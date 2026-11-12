package servlets;

import WebServices.ClienteSOAP;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.edextweb.soap.CursoDetalleDTO;
import com.mycompany.edextweb.soap.CursoProgramaDTO;
import com.mycompany.edextweb.soap.EdextWebService;
import com.mycompany.edextweb.soap.InscripcionCursoDTO;
import com.mycompany.edextweb.soap.InscripcionProgramaDTO;
import com.mycompany.edextweb.soap.PerfilUsuarioDTO;
import com.mycompany.edextweb.soap.ProgramaConsultaDTO;
import com.mycompany.edextweb.soap.UsuarioDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.WebServiceException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tramitar Acta de Estudio: genera el PDF cada vez que se solicita.
 * Uso: /SvltGenerarActa?programa=NOMBRE_DEL_PROGRAMA
 */
@WebServlet(name = "SvltGenerarActa", urlPatterns = {"/SvltGenerarActa"})
public class GenerarActaServlet extends HttpServlet {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private record Fila(String curso, String edicion, String resultado, String fecha) { }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // El acta es del usuario con sesión iniciada: el nickname sale de la sesión, no de un parámetro.
        HttpSession sesion = request.getSession(false);
        UsuarioDTO usuario = sesion == null ? null : (UsuarioDTO) sesion.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/componentes/login.jsp");
            return;
        }
        if (!"ESTUDIANTE".equalsIgnoreCase(usuario.getTipoUsuario())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo los estudiantes pueden tramitar actas.");
            return;
        }

        String programa = request.getParameter("programa");
        if (programa == null || programa.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el programa de formación.");
            return;
        }

        try {
            EdextWebService servicio = ClienteSOAP.obtenerServicio();
            String nickname = usuario.getNickname();

            // 1) El estudiante tiene que estar aceptado en el programa.
            if (!estaAceptadoEnPrograma(servicio, nickname, programa)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "No estás aceptado en este programa de formación.");
                return;
            }

            // 2) Cursos que componen el programa.
            ProgramaConsultaDTO datosPrograma = buscarPrograma(servicio, programa);
            if (datosPrograma == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No se encontró el programa de formación.");
                return;
            }

            // 3) Resultados del estudiante, curso por curso.
            List<InscripcionCursoDTO> resultados = servicio.verResultadosEvaluacionesEstudiante(nickname);
            List<Fila> filas = new ArrayList<>();
            for (CursoProgramaDTO curso : datosPrograma.getCursos()) {
                boolean conResultado = false;
                for (InscripcionCursoDTO r : resultados) {
                    if (curso.getNombre().equals(r.getNombreCurso())
                            && r.getNota() != null && !r.getNota().isBlank()) {
                        filas.add(new Fila(curso.getNombre(), r.getNombreEdicion(), r.getNota(),
                                fechaResultado(servicio, r.getNombreCurso(), r.getNombreEdicion())));
                        conResultado = true;
                    }
                }
                if (!conResultado) {
                    filas.add(new Fila(curso.getNombre(), "-", "Sin resultado", "-"));
                }
            }

            String nombreCompleto = (nullASinTexto(usuario.getNombre()) + " " + nullASinTexto(usuario.getApellido())).trim();
            byte[] pdf = generarPdf(nombreCompleto, nickname, programa, filas);
            String archivo = "acta_" + programa.replaceAll("[^A-Za-z0-9_-]", "_") + ".pdf";

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + archivo + "\"");
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf);

        } catch (WebServiceException e) {
            getServletContext().log("Error al obtener los datos del acta mediante SOAP.", e);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "No se pudo conectar con el servidor central.");
        } catch (DocumentException e) {
            getServletContext().log("Error al generar el PDF del acta.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo generar el PDF.");
        }
    }

    private boolean estaAceptadoEnPrograma(EdextWebService servicio, String nickname, String programa) {
        PerfilUsuarioDTO perfil = servicio.consultarPerfilUsuario(nickname);
        if (perfil == null) {
            return false;
        }
        for (InscripcionProgramaDTO inscripcion : perfil.getInscripcionesProgramas()) {
            if (programa.equals(inscripcion.getNombrePrograma()) && "ACEPTADA".equals(inscripcion.getEstado())) {
                return true;
            }
        }
        return false;
    }

    private ProgramaConsultaDTO buscarPrograma(EdextWebService servicio, String programa) {
        for (ProgramaConsultaDTO p : servicio.listarProgramasConsulta()) {
            if (programa.equals(p.getNombre())) {
                return p;
            }
        }
        return null;
    }

    /** Fecha del resultado de una edición: se toma la fecha de fin de la edición. */
    private String fechaResultado(EdextWebService servicio, String curso, String edicion) {
        try {
            CursoDetalleDTO detalle = servicio.consultarCursoDetalle(curso, edicion, null);
            if (detalle != null && detalle.getEdicionSeleccionada() != null) {
                String fin = detalle.getEdicionSeleccionada().getFechaFin();
                if (fin != null && !fin.isBlank()) {
                    try {
                        return LocalDate.parse(fin).format(FORMATO);
                    } catch (Exception e) {
                        return fin;
                    }
                }
            }
        } catch (WebServiceException e) {
            getServletContext().log("No se pudo obtener la fecha de la edición " + edicion, e);
        }
        return "-";
    }

    private byte[] generarPdf(String nombreCompleto, String nickname, String programa, List<Fila> filas)
            throws DocumentException {

        // Se arma en memoria para no dejar una respuesta a medias si algo falla.
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4, 50, 50, 60, 60);
        PdfWriter.getInstance(documento, salida);
        documento.open();

        Font titulo = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Font normal = new Font(Font.FontFamily.HELVETICA, 12);
        Font negrita = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        Paragraph encabezado = new Paragraph("edEXT - Acta de estudios", titulo);
        encabezado.setAlignment(Element.ALIGN_CENTER);
        encabezado.setSpacingAfter(24);
        documento.add(encabezado);

        documento.add(new Paragraph("Estudiante: " + nombreCompleto + " (" + nickname + ")", normal));
        documento.add(new Paragraph("Programa de formación: " + programa, normal));
        Paragraph emision = new Paragraph("Fecha de emisión: " + LocalDate.now().format(FORMATO), normal);
        emision.setSpacingAfter(18);
        documento.add(emision);

        PdfPTable tabla = new PdfPTable(new float[]{3.2f, 3.2f, 2.0f, 2.2f});
        tabla.setWidthPercentage(100);
        for (String cabecera : new String[]{"Curso", "Edición", "Resultado", "Fecha"}) {
            PdfPCell celda = new PdfPCell(new Phrase(cabecera, negrita));
            celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
            celda.setPadding(6);
            tabla.addCell(celda);
        }
        for (Fila f : filas) {
            tabla.addCell(celda(f.curso(), normal));
            tabla.addCell(celda(f.edicion(), normal));
            tabla.addCell(celda(f.resultado(), normal));
            tabla.addCell(celda(f.fecha(), normal));
        }
        documento.add(tabla);

        documento.close();
        return salida.toByteArray();
    }

    private PdfPCell celda(String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(nullASinTexto(texto), fuente));
        celda.setPadding(6);
        return celda;
    }

    private String nullASinTexto(String texto) {
        return texto == null ? "" : texto;
    }
}