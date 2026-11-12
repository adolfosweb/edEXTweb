package WebServices;

// Controladores
import Controladores.Buscador;
import Controladores.IControladorCurso;
import Controladores.IControladorInstituto;
import Controladores.IControladorUsuario;
import Controladores.ControladorTokenRecordarme;
import Controladores.ControladorSeguimiento;
import Controladores.IControladorPrograma;
import Controladores.Conexion;

// Datatypes
import Datatypes.EdicionCursoDTO;
import Datatypes.InstitutoDTO;
import Datatypes.ResultadoOperacionDTO;
import Datatypes.UsuarioDTO;
import Datatypes.PerfilUsuarioDTO;
import Datatypes.InscripcionCursoDTO;
import Datatypes.InscripcionProgramaDTO;
import Datatypes.EdicionPerfilDTO;
import Datatypes.CursoInscripcionDTO;
import Datatypes.ProgramaConsultaDTO;
import Datatypes.CursoProgramaDTO;
import Datatypes.CursoListadoDTO;
import Datatypes.ListadoCursosDTO;
import Datatypes.ResultadoBusquedaDTO;
import Datatypes.InscripcionSeleccionDTO;
import Datatypes.CursoDetalleDTO;
import Datatypes.EdicionDetalleDTO;
import Datatypes.ProgramaDetalleDTO;
import Datatypes.CursoAltaFormularioDTO;
import Datatypes.CursoEdicionFormularioDTO;
import Datatypes.CursoTendenciaDTO;
import Datatypes.EvaluacionDTO;

// Fabric
import Fabric.Fabric;

// Lógica
import Logica.EdicionCurso;
import Logica.Curso;
import Logica.Estudiante;
import Logica.Docente;
import Logica.InscripcionCurso;
import Logica.InscripcionPrograma;
import Logica.EstadoInscripcion;
import Logica.Instituto;
import Logica.ProgramaFormacion;
import Logica.TokenRecordarme;
import Logica.Usuario;
import Logica.Categoria;

// SOAP
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

// Java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@WebService(serviceName = "EdextService",name = "EdextWebService",targetNamespace = "http://webservices.edext.com/")
public class EdextWebService {

    // =====================================================
    // DEPENDENCIAS Y CONSTRUCTOR
    // =====================================================

    private final IControladorInstituto controladorInstituto;
    private final IControladorCurso controladorCurso;
    private final IControladorUsuario controladorUsuario;
    private final IControladorPrograma controladorPrograma;
    private final ControladorTokenRecordarme controladorToken;
    private final ControladorSeguimiento controladorSeguimiento;
    private final Buscador buscador;

    public EdextWebService() {

        Fabric fabric = Fabric.getInstancia();

        controladorInstituto = fabric.getControladorInstituto();
        controladorCurso = fabric.getControladorCurso();
        controladorUsuario = fabric.getControladorUsuario();
        controladorPrograma = fabric.getControladorPrograma();

        controladorToken = new ControladorTokenRecordarme();
        controladorSeguimiento = new ControladorSeguimiento();
        buscador = new Buscador();
    }

    // =====================================================
    // AUTENTICACIÓN Y GESTIÓN DE USUARIOS
    // =====================================================
    
    @WebMethod
    public UsuarioDTO registrarUsuario(@WebParam(name = "usuario") UsuarioDTO usuarioDTO,@WebParam(name = "password") String password,@WebParam(name = "nombreInstituto") String nombreInstituto) {
        if (usuarioDTO == null) {
            return null;
        }
        try {
            LocalDate fecha = LocalDate.parse(usuarioDTO.getFecNac());
            Usuario usuario;

            if ("DOCENTE".equalsIgnoreCase(usuarioDTO.getTipoUsuario())) {

                usuario = new Docente(
                        usuarioDTO.getNickname(),
                        usuarioDTO.getCorreoElectronico(),
                        usuarioDTO.getNombre(),
                        usuarioDTO.getApellido(),
                        fecha);

            } else if ("ESTUDIANTE".equalsIgnoreCase(usuarioDTO.getTipoUsuario())) {

                usuario = new Estudiante(
                        usuarioDTO.getNickname(),
                        usuarioDTO.getCorreoElectronico(),
                        usuarioDTO.getNombre(),
                        usuarioDTO.getApellido(),
                        fecha);

            } else {
                return null;
            }
            usuario.setPassword(password);
            boolean resultado = controladorUsuario.altaUsuario(usuario,
                    "DOCENTE".equalsIgnoreCase(usuarioDTO.getTipoUsuario()) ? nombreInstituto : null);
            if (!resultado) {
                return null;
            }
            return convertirUsuario(usuario);
        } catch (Exception e) {
            return null;
        }
    }
    
    @WebMethod
    public UsuarioDTO iniciarSesion(@WebParam(name = "identificador") String identificador,@WebParam(name = "password") String password) {
        Usuario usuario = controladorUsuario.iniciarSesion(identificador, password);
        if (usuario == null) {
            return null;
        }
        return convertirUsuario(usuario);
    }

    @WebMethod
    public UsuarioDTO buscarUsuarioPorNickname(@WebParam(name = "nickname") String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return null;
        }
        Usuario usuario = controladorUsuario.buscarUsuario(nickname);
        if (usuario == null) {
            return null;
        }
        return convertirUsuario(usuario);
    }

    private UsuarioDTO convertirUsuario(Usuario usuario) {
        String tipoUsuario = usuario instanceof Estudiante ? "ESTUDIANTE" : "DOCENTE";
        return new UsuarioDTO(
                usuario.getNickname(),
                usuario.getCorreoElectronico(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getFecNac() != null ? usuario.getFecNac().toString() : "",
                tipoUsuario,
                usuario.getImagen());
    }
    
    // =========================
    // SEGUIMIENTO DE USUARIOS
    // =========================

    @WebMethod
    public boolean estaSiguiendo(@WebParam(name = "nicknameSeguidor") String nicknameSeguidor,
            @WebParam(name = "nicknameSeguido") String nicknameSeguido) {
        return controladorSeguimiento.estaSiguiendo(nicknameSeguidor,nicknameSeguido);
    }

    @WebMethod
    public boolean seguirUsuario(@WebParam(name = "nicknameSeguidor") String nicknameSeguidor,
            @WebParam(name = "nicknameSeguido") String nicknameSeguido) {
        return controladorSeguimiento.seguir(nicknameSeguidor,nicknameSeguido);
    }

    @WebMethod
    public boolean dejarDeSeguir(@WebParam(name = "nicknameSeguidor") String nicknameSeguidor,
            @WebParam(name = "nicknameSeguido") String nicknameSeguido) {
        return controladorSeguimiento.dejarDeSeguir(nicknameSeguidor,nicknameSeguido);
    }

    // =====================================================
    // GESTIÓN DE TOKENS "RECORDARME"
    // =====================================================

    @WebMethod
    public boolean guardarTokenRecordarme(@WebParam(name = "tokenHash") String tokenHash,@WebParam(name = "nickname") String nickname) {
        if (tokenHash == null || tokenHash.isBlank() || nickname == null || nickname.isBlank()) {
            return false;
        }
        TokenRecordarme token = new TokenRecordarme();

        token.setTokenHash(tokenHash);
        token.setNicknameUsuario(nickname);
        token.setFechaExpiracion(LocalDateTime.now().plusDays(30));

        controladorToken.guardarToken(token);
        return true;
    }

    @WebMethod
    public String buscarNicknameTokenValido(@WebParam(name = "tokenHash") String tokenHash) {
        if (tokenHash == null || tokenHash.isBlank()) {
            return null;
        }
        TokenRecordarme token = controladorToken.buscarTokenValido(tokenHash);
        return token != null ? token.getNicknameUsuario() : null;
    }

    @WebMethod
    public boolean eliminarTokenRecordarme(@WebParam(name = "tokenHash") String tokenHash) {
        if (tokenHash == null || tokenHash.isBlank()) {
            return false;
        }
        controladorToken.eliminarToken(tokenHash);
        return true;
    }
    
    // =========================
    // CONSULTA DE USUARIO
    // =========================
    
    @WebMethod
    public List<UsuarioDTO> listarUsuariosConsulta() {
        List<UsuarioDTO> resultado = new ArrayList<>();
        Map<String, Usuario> usuarios = controladorUsuario.listarUsuarios();
        if (usuarios == null || usuarios.isEmpty()) {
            return resultado;
        }
        for (Usuario usuario : usuarios.values()) {
            if (usuario == null) {
                continue;
            }
            UsuarioDTO dto = new UsuarioDTO();

            dto.setNickname(usuario.getNickname());
            dto.setCorreoElectronico(usuario.getCorreoElectronico());
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setFecNac(usuario.getFecNac() != null ? usuario.getFecNac().toString() : null);
            dto.setTipoUsuario(usuario instanceof Estudiante ? "ESTUDIANTE" : "DOCENTE");
            dto.setImagen(usuario.getImagen());
            resultado.add(dto);
        }
        return resultado;
    }
    
    // =========================
    // PERFIL DE USUARIO
    // =========================

    @WebMethod
    public PerfilUsuarioDTO consultarPerfilUsuario(@WebParam(name = "nickname") String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            return null;
        }
        Usuario usuario = controladorUsuario.buscarUsuario(nickname);
        if (usuario == null) {
            return null;
        }

        PerfilUsuarioDTO perfil = new PerfilUsuarioDTO();
        perfil.setUsuario(convertirUsuario(usuario));

        // PERFIL DE ESTUDIANTE
        
        if (usuario instanceof Estudiante estudiante) {

            List<InscripcionCursoDTO> cursos = new ArrayList<>();
            for (InscripcionCurso inscripcion : estudiante.getCursos()) {
                if (inscripcion.getCurso() == null) {
                    continue;
                }
                var edicion = inscripcion.getCurso();
                String nombreCurso = edicion.getCurso() != null ? edicion.getCurso().getNombre() : null;
                cursos.add(new InscripcionCursoDTO(
                        edicion.getNombre(),
                        nombreCurso,
                        inscripcion.getFechaIns() != null ? inscripcion.getFechaIns().toString() : null,
                        inscripcion.getEstado() != null ? inscripcion.getEstado().name() : null));
            }

            perfil.setInscripcionesCursos(cursos);
            List<InscripcionProgramaDTO> programas = new ArrayList<>();
            for (InscripcionPrograma inscripcion : estudiante.getProgramas()) {
                if (inscripcion.getPrograma() == null) {
                    continue;
                }
                programas.add(new InscripcionProgramaDTO(
                        inscripcion.getPrograma().getNombre(),
                        inscripcion.getFechaIns() != null ? inscripcion.getFechaIns().toString() : null,
                        inscripcion.getEstado() != null ? inscripcion.getEstado().name() : null));
            }
            perfil.setInscripcionesProgramas(programas);
        }

        // PERFIL DE DOCENTE

        else if (usuario instanceof Docente docente) {

            List<EdicionPerfilDTO> ediciones = new ArrayList<>();
            for (EdicionCurso edicion : docente.getEdiciones()) {

                String nombreCurso = edicion.getCurso() != null ? edicion.getCurso().getNombre() : null;
                EdicionPerfilDTO edicionDTO = new EdicionPerfilDTO(edicion.getNombre(),nombreCurso);
                List<UsuarioDTO> estudiantesAceptados = new ArrayList<>();
                for (InscripcionCurso inscripcion : edicion.getInscriptos()) {

                    if (inscripcion.getEstado() == EstadoInscripcion.ACEPTADA
                            && inscripcion.getEstudiante() != null) {

                        estudiantesAceptados.add(convertirUsuario(inscripcion.getEstudiante()));
                    }
                }

                edicionDTO.setEstudiantesAceptados(estudiantesAceptados);
                ediciones.add(edicionDTO);
            }
            perfil.setEdicionesDocente(ediciones);
        }
        return perfil;
    }
    
    // =====================================
    // MODIFICACIÓN DE DATOS DE USUARIO
    // =====================================

    @WebMethod
    public ResultadoOperacionDTO modificarDatosUsuario(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "apellido") String apellido,
            @WebParam(name = "correo") String correo,
            @WebParam(name = "fechaNacimiento") String fechaTexto) {

        if (nickname == null || nickname.isBlank()) {
            return new ResultadoOperacionDTO(false, "No se indicó el usuario.");
        }
        if (nombre == null || nombre.isBlank()
                || apellido == null || apellido.isBlank()
                || correo == null || correo.isBlank()
                || fechaTexto == null || fechaTexto.isBlank()) {

            return new ResultadoOperacionDTO(false,"Todos los campos son obligatorios.");
        }

        nombre = nombre.trim();
        apellido = apellido.trim();
        correo = correo.trim();

        if (!correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            return new ResultadoOperacionDTO(false,"Ingresá un correo electrónico válido.");
        }
        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.parse(fechaTexto.trim());
        } catch (DateTimeParseException e) {
            return new ResultadoOperacionDTO(false,"La fecha de nacimiento no es válida.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            return new ResultadoOperacionDTO(false,"La fecha de nacimiento no puede ser futura.");
        }

        Usuario usuario = controladorUsuario.buscarUsuario(nickname);
        if (usuario == null) {
            return new ResultadoOperacionDTO(false,"No se encontró el usuario.");
        }

        if (controladorUsuario.correoUsadoPorOtroUsuario(correo, nickname)) {
            return new ResultadoOperacionDTO(false,"Ese correo electrónico ya está registrado.");
        }

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreoElectronico(correo);
        usuario.setFecNac(fechaNacimiento);

        controladorUsuario.modificarUsuario(usuario);

        return new ResultadoOperacionDTO(true,"Los datos del usuario se modificaron correctamente.");
    }
    
    // =====================================
    // ALTA DE CURSO
    // =====================================
    
    @WebMethod
    public CursoAltaFormularioDTO obtenerFormularioAltaCurso(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreCurso") String nombreCurso,
            @WebParam(name = "modoEdicion") boolean modoEdicion) {

        CursoAltaFormularioDTO dto = new CursoAltaFormularioDTO();
        dto.setModoEdicion(modoEdicion);

        Usuario usuario = controladorUsuario.buscarUsuario(nickname);

        if (!(usuario instanceof Docente)) {
            dto.setAutorizado(false);
            return dto;
        }

        Docente docente = (Docente) usuario;
        dto.setAutorizado(true);
        for (Instituto instituto : docente.getInstitutos()) {
            dto.getInstitutos().add(instituto.getNombre());
        }
        if (modoEdicion) {
            Curso existente = controladorCurso.buscarCurso(nombreCurso);
            if (existente == null) {
                dto.setCursoEncontrado(false);
                return dto;
            }

            dto.setCursoEncontrado(true);
            if (existente.getInstituto() == null
                    || !docenteTieneInstituto(docente, existente.getInstituto().getNombre())) {
                dto.setAutorizado(false);
                return dto;
            }

            dto.setNombre(existente.getNombre());
            dto.setDescripcion(existente.getDescripcion());
            dto.setDuracion(existente.getDuracion());
            dto.setCantHoras(existente.getCantHoras());
            dto.setCantCreditos(existente.getCantCreditos());
            dto.setUrl(existente.getUrl());
            dto.setInstituto(existente.getInstituto().getNombre());

            if (existente.getFecRegistro() != null) {
                dto.setFechaRegistro(
                        new java.text.SimpleDateFormat("yyyy-MM-dd").format(existente.getFecRegistro()));
            }
            for (Curso previa : existente.getPrevias()) {
                dto.getPreviasSeleccionadas().add(previa.getNombre());
            }
            for (Categoria categoria : existente.getCategorias()) {
                dto.getCategoriasSeleccionadas().add(categoria.getNombre());
            }
        } else {
            dto.setCursoEncontrado(true);
            dto.setFechaRegistro(LocalDate.now().toString());
        }
        Map<String, Curso> cursos = controladorCurso.listarCursos();
        if (cursos != null) {
            for (Curso curso : cursos.values()) {
                if (!modoEdicion || !curso.getNombre().equals(nombreCurso)) {
                    dto.getCursosExistentes().add(curso.getNombre());
                }
            }
        }
        
        Map<String, Categoria> categorias = controladorCurso.listarCategorias();
        if (categorias != null) {
            dto.getCategorias().addAll(categorias.keySet());
        }
        return dto;
    }
    
    @WebMethod
    public ResultadoOperacionDTO guardarCurso(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "descripcion") String descripcion,
            @WebParam(name = "duracion") String duracion,
            @WebParam(name = "cantHoras") int cantHoras,
            @WebParam(name = "cantCreditos") int cantCreditos,
            @WebParam(name = "url") String url,
            @WebParam(name = "nombreInstituto") String nombreInstituto,
            @WebParam(name = "fechaRegistro") String fechaRegistroTexto,
            @WebParam(name = "modoEdicion") boolean modoEdicion,
            @WebParam(name = "previas") List<String> nombresPrevias,
            @WebParam(name = "categorias") List<String> nombresCategorias) {

        nombre = limpiarTexto(nombre);
        descripcion = limpiarTexto(descripcion);
        duracion = limpiarTexto(duracion);
        url = limpiarTexto(url);
        nombreInstituto = limpiarTexto(nombreInstituto);

        if (nombre == null || nombre.isEmpty()
                || descripcion == null || descripcion.isEmpty()
                || duracion == null || duracion.isEmpty()
                || url == null || url.isEmpty()
                || nombreInstituto == null || nombreInstituto.isEmpty()) {
            return new ResultadoOperacionDTO(false,"Completa todos los campos obligatorios.");
        }

        Usuario usuario = controladorUsuario.buscarUsuario(nickname);
        if (!(usuario instanceof Docente)) {
            return new ResultadoOperacionDTO(false,"Solo un docente puede dar de alta o modificar cursos.");
        }
        
        Docente docente = (Docente) usuario;
        if (!docenteTieneInstituto(docente, nombreInstituto)) {
            return new ResultadoOperacionDTO(false,"Selecciona uno de los institutos asociados a tu cuenta docente.");
        }
        if (cantHoras < 0 || cantCreditos < 0) {
            return new ResultadoOperacionDTO(false,"Horas y créditos no pueden ser negativos.");
        }
        LocalDate fechaRegistro = null;
        if (!modoEdicion) {
            if (fechaRegistroTexto == null || fechaRegistroTexto.isBlank()) {
                return new ResultadoOperacionDTO(false,
                        "Selecciona la fecha de registro del curso.");
            }
            try {
                fechaRegistro = LocalDate.parse(fechaRegistroTexto);
                if (fechaRegistro.isBefore(LocalDate.now())) {
                    return new ResultadoOperacionDTO(false,"La fecha de registro no puede ser anterior a hoy.");
                }
            } catch (DateTimeParseException ex) {
                return new ResultadoOperacionDTO(false,"Selecciona una fecha de registro válida.");
            }
        }
        Curso cursoExistente = null;
        if (modoEdicion) {
            cursoExistente = controladorCurso.buscarCurso(nombre);
            if (cursoExistente == null
                    || cursoExistente.getInstituto() == null
                    || !docenteTieneInstituto(docente, cursoExistente.getInstituto().getNombre())) {
                return new ResultadoOperacionDTO(false,"No tienes permiso para modificar este curso.");
            }
        } else if (controladorCurso.buscarCurso(nombre) != null) {
            return new ResultadoOperacionDTO(false,"Ya existe un curso con ese nombre.");
        }
        List<Curso> previas = new ArrayList<>();
        if (nombresPrevias != null) {
            for (String nombrePrevia : nombresPrevias) {
                if (nombre.equals(nombrePrevia)) {
                    return new ResultadoOperacionDTO(false,"Un curso no puede ser previa de sí mismo.");
                }
                Curso previa = controladorCurso.buscarCurso(nombrePrevia);
                if (previa == null) {
                    return new ResultadoOperacionDTO(false,"Una de las previas seleccionadas ya no existe.");
                }
                if (!previas.contains(previa)) {
                    previas.add(previa);
                }
            }
        }
        List<Categoria> categoriasSeleccionadas = new ArrayList<>();
        Map<String, Categoria> disponibles = controladorCurso.listarCategorias();
        if (nombresCategorias != null) {
            for (String nombreCategoria : nombresCategorias) {
                Categoria categoria = disponibles != null ? disponibles.get(nombreCategoria) : null;
                if (categoria == null) {
                    return new ResultadoOperacionDTO(false,"Una de las categorías seleccionadas ya no existe.");
                }
                if (!categoriasSeleccionadas.contains(categoria)) {
                    categoriasSeleccionadas.add(categoria);
                }
            }
        }
        Curso curso = new Curso(
                nombre,
                descripcion,
                duracion,
                cantHoras,
                cantCreditos,
                modoEdicion? cursoExistente.getFecRegistro() : java.sql.Date.valueOf(fechaRegistro),
                url);
        curso.setInstituto(new Instituto(nombreInstituto));
        curso.setPrevias(previas);
        curso.setCategorias(categoriasSeleccionadas);

        // Al modificar, se conservan las visitas y la imagen del curso existente.
        if (modoEdicion) {
            curso.setVisitas(cursoExistente.getVisitas());
            curso.setImagen(cursoExistente.getImagen());
        }

        boolean guardado = modoEdicion ? controladorCurso.modificarCurso(curso) : controladorCurso.altaCurso(curso);
        if (!guardado) {
            return new ResultadoOperacionDTO(false,
                    modoEdicion ? "No se pudo modificar el curso. Revisa los datos e inténtalo nuevamente." : "No se pudo dar de alta el curso. Revisa los datos e inténtalo nuevamente.");
        }
        return new ResultadoOperacionDTO(true,
                modoEdicion ? "El curso fue modificado correctamente." : "El curso fue dado de alta correctamente.");
    }
    
    private boolean docenteTieneInstituto(Docente docente, String nombreInstituto) {
        if (docente == null || nombreInstituto == null) {
            return false;
        }
        for (Instituto instituto : docente.getInstitutos()) {
            if (nombreInstituto.equals(instituto.getNombre())) {
                return true;
            }
        }
        return false;
    }
    private String limpiarTexto(String valor) {
        return valor == null ? null : valor.trim();
    }
    
    // =====================================================
    // CONSULTA DE CURSOS
    // =====================================================
    
    @WebMethod
    public CursoDetalleDTO consultarCursoDetalle(
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "nombreEdicion") String nombreEdicion,
            @WebParam(name = "nombrePrograma") String nombrePrograma) {

        if (nombre == null || nombre.isBlank()) {
            return null;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            List<Curso> cursos = em.createQuery(
                    "SELECT DISTINCT c FROM Curso c "
                    + "LEFT JOIN FETCH c.instituto "
                    + "LEFT JOIN FETCH c.previas "
                    + "WHERE c.nombre = :nombre", Curso.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
            if (cursos.isEmpty()) {
                return null;
            }

            Curso curso = cursos.get(0);
            // Inicializamos las categorías dentro del contexto de persistencia.
            em.createQuery(
                    "SELECT DISTINCT c FROM Curso c "
                    + "LEFT JOIN FETCH c.categorias "
                    + "WHERE c.nombre = :nombre", Curso.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            CursoDetalleDTO dto = new CursoDetalleDTO();
            dto.setNombre(curso.getNombre());
            dto.setDescripcion(curso.getDescripcion());
            dto.setDuracion(curso.getDuracion());
            dto.setCantHoras(curso.getCantHoras());
            dto.setCantCreditos(curso.getCantCreditos());
            dto.setFechaRegistro(formatearFecha(curso.getFecRegistro()));
            dto.setUrl(curso.getUrl());
            dto.setImagen(curso.getImagen());
            dto.setInstituto(curso.getInstituto() != null ? curso.getInstituto().getNombre() : null);

            List<String> previas = new ArrayList<>();
            for (Curso previa : curso.getPrevias()) {
                previas.add(previa.getNombre());
            }
            dto.setPrevias(previas);
            List<String> categorias = new ArrayList<>();
            for (Categoria categoria : curso.getCategorias()) {
                categorias.add(categoria.getNombre());
            }
            dto.setCategorias(categorias);
            List<EdicionCurso> edicionesEntidad = em.createQuery(
                    "SELECT e FROM EdicionCurso e "
                    + "WHERE e.curso.nombre = :nombre ORDER BY e.nombre",
                    EdicionCurso.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
            List<EdicionDetalleDTO> ediciones = new ArrayList<>();
            for (EdicionCurso edicion : edicionesEntidad) {
                ediciones.add(convertirEdicionDetalle(edicion));
            }
            dto.setEdiciones(ediciones);
            List<ProgramaFormacion> programasEntidad = em.createQuery(
                    "SELECT DISTINCT p FROM ProgramaFormacion p JOIN p.cursos c "
                    + "WHERE c.nombre = :nombre ORDER BY p.nombre",
                    ProgramaFormacion.class)
                    .setParameter("nombre", nombre)
                    .getResultList();

            List<ProgramaDetalleDTO> programas = new ArrayList<>();
            for (ProgramaFormacion programa : programasEntidad) {
                programas.add(convertirProgramaDetalle(programa));
            }
            dto.setProgramas(programas);
            // La selección se obtiene de las listas ya consultadas.
            if (nombreEdicion != null && !nombreEdicion.isBlank()) {
                for (EdicionDetalleDTO edicion : ediciones) {
                    if (nombreEdicion.equals(edicion.getNombre())) {
                        dto.setEdicionSeleccionada(edicion);
                        break;
                    }
                }
                if (dto.getEdicionSeleccionada() == null) {
                    return null;
                }
            } else if (nombrePrograma != null && !nombrePrograma.isBlank()) {
                for (ProgramaDetalleDTO programa : programas) {
                    if (nombrePrograma.equals(programa.getNombre())) {
                        dto.setProgramaSeleccionado(programa);
                        break;
                    }
                }
                if (dto.getProgramaSeleccionado() == null) {
                    return null;
                }
            }
            return dto;
        } finally {
            em.close();
        }
    }

    private EdicionDetalleDTO convertirEdicionDetalle(EdicionCurso edicion) {
        EdicionDetalleDTO dto = new EdicionDetalleDTO();

        dto.setNombre(edicion.getNombre());
        dto.setFechaInicio(formatearFecha(edicion.getFechaInicio()));
        dto.setFechaFin(formatearFecha(edicion.getFechaFin()));
        dto.setCupo(edicion.getCupo());
        dto.setFechaPublicacion(formatearFecha(edicion.getFechaPublicacion()));
        dto.setNombreCurso(edicion.getCurso() != null ? edicion.getCurso().getNombre() : null);
        dto.setImagen(edicion.getImagen());

        List<String> docentes = new ArrayList<>();
        for (Docente docente : edicion.getDocentes()) {
            docentes.add(docente.getNickname());
        }
        dto.setNombresDocentes(docentes);

        List<String> inscriptos = new ArrayList<>();
        for (InscripcionCurso inscripcion : edicion.getInscriptos()) {
            Estudiante estudiante = inscripcion.getEstudiante();
            if (estudiante != null) {
                inscriptos.add(estudiante.getNickname());
            }
        }
        dto.setNombresInscriptos(inscriptos);
        return dto;
    }

    private ProgramaDetalleDTO convertirProgramaDetalle(ProgramaFormacion programa) {
        ProgramaDetalleDTO dto = new ProgramaDetalleDTO();

        dto.setNombre(programa.getNombre());
        dto.setDescripcion(programa.getDescripcion());
        dto.setFechaInicio(formatearFecha(programa.getFechaInicio()));
        dto.setFechaFin(formatearFecha(programa.getFechaFin()));
        dto.setFechaAlta(formatearFecha(programa.getFechaAlta()));
        dto.setImagen(programa.getImagen());

        List<String> cursos = new ArrayList<>();
        for (Curso curso : programa.getCursos()) {
            cursos.add(curso.getNombre());
        }
        dto.setNombresCursos(cursos);
        List<String> inscriptos = new ArrayList<>();
        for (InscripcionPrograma inscripcion : programa.getInscriptos()) {
            Estudiante estudiante = inscripcion.getEstudiante();
            if (estudiante != null) {
                inscriptos.add(estudiante.getNickname());
            }
        }
        dto.setNombresInscriptos(inscriptos);
        return dto;
    }

    private String formatearFecha(Object fecha) {
        if (fecha == null) {
            return null;
        }
        if (fecha instanceof LocalDate) {
            return ((LocalDate) fecha).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        if (fecha instanceof Date) {
            return new SimpleDateFormat("dd/MM/yyyy").format((Date) fecha);
        }

        return fecha.toString();
    }

    // =====================================================
    // TENDENCIAS (VISITAS A CURSOS)
    // =====================================================
    
    @WebMethod
    public void registrarVisitaCurso(@WebParam(name = "nombreCurso") String nombreCurso) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("UPDATE Curso c SET c.visitas = c.visitas + 1 WHERE c.nombre = :n")
              .setParameter("n", nombreCurso)
              .executeUpdate();
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @WebMethod
    public List<CursoTendenciaDTO> listarCursosMasVisitados() {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            List<Curso> cursos = em.createQuery(
                "SELECT c FROM Curso c ORDER BY c.visitas DESC, c.nombre ASC", Curso.class)
                .getResultList();
            List<CursoTendenciaDTO> resultado = new ArrayList<>();
            for (Curso c : cursos) {
                resultado.add(new CursoTendenciaDTO(c.getNombre(), c.getVisitas()));
            }
            return resultado;
        } finally {
            em.close();
        }
    }
    
    // =====================================
    // ALTA DE EDICIONES
    // =====================================
    
    @WebMethod
    public CursoEdicionFormularioDTO obtenerFormularioAltaEdicionCurso(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreCurso") String nombreCurso,
            @WebParam(name = "modoEdicion") boolean modoEdicion,
            @WebParam(name = "nombreEdicion") String nombreEdicion) {

        CursoEdicionFormularioDTO dto = new CursoEdicionFormularioDTO();
        dto.setModoEdicion(modoEdicion);

        Usuario usuario = controladorUsuario.buscarUsuario(nickname);
        if (!(usuario instanceof Docente docente)) {
            return dto;
        }
        Curso curso = buscarCursoParaEdicion(nombreCurso);
        if (curso == null) {
            return dto;
        }

        dto.setCursoEncontrado(true);
        dto.setNombreCurso(curso.getNombre());
        if (curso.getInstituto() == null || !docentePuedeGestionarCursoEdicion(docente, curso)) {
            return dto;
        }

        dto.setAutorizado(true);
        Map<String, Usuario> usuarios = controladorUsuario.listarUsuarios();
        if (usuarios != null) {
            for (Usuario u : usuarios.values()) {
                if (u instanceof Docente) {
                    dto.getDocentes().add(u.getNickname());
                }
            }
        }
        if (!modoEdicion) {
            dto.setEdicionEncontrada(true);
            dto.getDocentesSeleccionados().add(docente.getNickname());
            return dto;
        }

        EdicionCurso edicion = buscarEdicionParaEdicion(nombreEdicion);
        if (edicion == null) {
            return dto;
        }
        if (edicion.getCurso() == null || !curso.getNombre().equals(edicion.getCurso().getNombre())) {
            dto.setAutorizado(false);
            return dto;
        }

        dto.setEdicionEncontrada(true);
        dto.setNombreEdicion(edicion.getNombre());
        dto.setFechaInicio(edicion.getFechaInicio() != null ? edicion.getFechaInicio().toString() : "");
        dto.setFechaFin(edicion.getFechaFin() != null ? edicion.getFechaFin().toString() : "");
        dto.setCupo(edicion.getCupo() == 0 ? "" : String.valueOf(edicion.getCupo()));

        for (Docente participante : edicion.getDocentes()) {
            dto.getDocentesSeleccionados().add(participante.getNickname());
        }

        return dto;
    }
    
    @WebMethod
    public ResultadoOperacionDTO guardarEdicionCurso(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreCurso") String nombreCurso,
            @WebParam(name = "nombreEdicion") String nombreEdicion,
            @WebParam(name = "fechaInicio") String inicioTexto,
            @WebParam(name = "fechaFin") String finTexto,
            @WebParam(name = "cupo") String cupoTexto,
            @WebParam(name = "modoEdicion") boolean modoEdicion,
            @WebParam(name = "edicionOriginal") String edicionOriginal,
            @WebParam(name = "docentes") List<String> nombresDocentes) {

        try {
            Usuario usuario = controladorUsuario.buscarUsuario(nickname);
            if (!(usuario instanceof Docente docente)) {
                return new ResultadoOperacionDTO(false,"Solo un docente puede gestionar ediciones.");
            }
            
            Curso curso = buscarCursoParaEdicion(nombreCurso);
            if (curso == null) {
                return new ResultadoOperacionDTO(false,"No se encontró el curso.");
            }
            if (!docentePuedeGestionarCursoEdicion(docente, curso)) {
                return new ResultadoOperacionDTO(false,"No puedes gestionar ediciones de este curso.");
            }
            if (nombreEdicion == null || nombreEdicion.isBlank()
                    || inicioTexto == null || inicioTexto.isBlank()
                    || finTexto == null || finTexto.isBlank()) {
                return new ResultadoOperacionDTO(false,"Completa el nombre y las fechas de inicio y fin.");
            }

            LocalDate fechaInicio;
            LocalDate fechaFin;
            try {
                fechaInicio = LocalDate.parse(inicioTexto);
                fechaFin = LocalDate.parse(finTexto);
            } catch (DateTimeParseException ex) {
                return new ResultadoOperacionDTO(false,"Ingresa fechas válidas.");
            }
            if (!modoEdicion && fechaInicio.isBefore(LocalDate.now())) {
                return new ResultadoOperacionDTO(false,"La fecha de inicio no puede ser anterior a hoy.");
            }
            if (!fechaFin.isAfter(fechaInicio)) {
                return new ResultadoOperacionDTO(false,"La fecha de fin debe ser posterior a la fecha de inicio.");
            }

            int cupo;
            try {
                cupo = cupoTexto == null || cupoTexto.isBlank() ? 0 : Integer.parseInt(cupoTexto);
            } catch (NumberFormatException ex) {
                return new ResultadoOperacionDTO(false,"El cupo debe ser un número entero.");
            }
            if (cupo < 0) {
                return new ResultadoOperacionDTO(false,"El cupo debe ser cero o un número positivo.");
            }
            if (nombresDocentes == null || nombresDocentes.isEmpty()) {
                return new ResultadoOperacionDTO(false,"Selecciona al menos un docente participante.");
            }
            
            List<Docente> docentesSeleccionados = new ArrayList<>();
            for (String nombreDocente : nombresDocentes) {
                Usuario participante = controladorUsuario.buscarUsuario(nombreDocente);
                if (!(participante instanceof Docente docenteParticipante)) {
                    return new ResultadoOperacionDTO(false,"Uno de los participantes seleccionados no es un docente válido.");
                }
                if (docentesSeleccionados.stream().noneMatch(
                        d -> d.getNickname().equals(docenteParticipante.getNickname()))) {
                    docentesSeleccionados.add(docenteParticipante);
                }
            }
            EdicionCurso edicionExistente = null;
            if (modoEdicion) {
                if (edicionOriginal == null || edicionOriginal.isBlank()) {
                    return new ResultadoOperacionDTO(false,"No se identificó la edición que se desea modificar.");
                }
                edicionExistente = buscarEdicionParaEdicion(edicionOriginal);
                if (edicionExistente == null
                        || edicionExistente.getCurso() == null
                        || !nombreCurso.equals(edicionExistente.getCurso().getNombre())) {
                    return new ResultadoOperacionDTO(false,"La edición no pertenece al curso seleccionado.");
                }

                if (!edicionOriginal.equals(nombreEdicion)) {
                    return new ResultadoOperacionDTO(false,"El nombre de una edición existente no se puede cambiar.");
                }
            }

            EdicionCurso edicionConNombre = buscarEdicionParaEdicion(nombreEdicion);

            if (edicionConNombre != null
                    && (!modoEdicion
                    || !edicionConNombre.getNombre().equals(edicionOriginal))) {
                return new ResultadoOperacionDTO(false,"Ya existe una edición con ese nombre.");
            }

            if (modoEdicion) {
                boolean modificada = modificarEdicionCentral(
                        edicionOriginal,
                        fechaInicio,
                        fechaFin,
                        cupo,
                        docentesSeleccionados);

                return new ResultadoOperacionDTO(
                        modificada,
                        modificada ? "Edición modificada correctamente." : "No se pudo modificar la edición.");
            }
            EdicionCurso nuevaEdicion = new EdicionCurso(
                    nombreEdicion,
                    fechaInicio,
                    fechaFin,
                    cupo,
                    LocalDate.now(),
                    curso,
                    docentesSeleccionados.get(0));
            for (int i = 1; i < docentesSeleccionados.size(); i++) {
                nuevaEdicion.getDocentes().add(docentesSeleccionados.get(i));
            }
            boolean creada = controladorCurso.altaEdicion(nuevaEdicion);
            return new ResultadoOperacionDTO(creada,
                    creada ? "Edición creada correctamente." : "No se pudo crear la edición.");
        } catch (Exception ex) {
            return new ResultadoOperacionDTO(false,"Ocurrió un error al guardar la edición.");
        }
    }
    
    private Curso buscarCursoParaEdicion(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return null;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            List<Curso> cursos = em.createQuery(
                    "SELECT c FROM Curso c "
                    + "LEFT JOIN FETCH c.instituto "
                    + "WHERE c.nombre = :nombre",
                    Curso.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
            return cursos.isEmpty() ? null : cursos.get(0);
        } finally {
            em.close();
        }
    }

    private EdicionCurso buscarEdicionParaEdicion(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return null;
        }
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            List<EdicionCurso> ediciones = em.createQuery(
                    "SELECT DISTINCT e FROM EdicionCurso e "
                    + "LEFT JOIN FETCH e.curso "
                    + "LEFT JOIN FETCH e.docentes "
                    + "WHERE e.nombre = :nombre",
                    EdicionCurso.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
            return ediciones.isEmpty() ? null : ediciones.get(0);
        } finally {
            em.close();
        }
    }

    private boolean docentePuedeGestionarCursoEdicion(Docente docente, Curso curso) {
        if (docente == null || curso == null || curso.getInstituto() == null) {
            return false;
        }
        String nombreInstituto = curso.getInstituto().getNombre();
        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            Long cantidad = em.createQuery(
                    "SELECT COUNT(i) FROM Docente d "
                    + "JOIN d.institutos i "
                    + "WHERE d.nickname = :nickname "
                    + "AND i.nombre = :instituto",
                    Long.class)
                    .setParameter("nickname", docente.getNickname())
                    .setParameter("instituto", nombreInstituto)
                    .getSingleResult();
            return cantidad != null && cantidad > 0;
        } finally {
            em.close();
        }
    }

    private boolean modificarEdicionCentral(String nombre,LocalDate inicio,LocalDate fin,int cupo,List<Docente> docentesSeleccionados) {
        EntityManager em = Conexion.getInstancia().getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            EdicionCurso edicion = em.createQuery(
                    "SELECT DISTINCT e FROM EdicionCurso e "
                    + "LEFT JOIN FETCH e.curso "
                    + "LEFT JOIN FETCH e.docentes "
                    + "WHERE e.nombre = :nombre",
                    EdicionCurso.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            edicion.setFechaInicio(inicio);
            edicion.setFechaFin(fin);
            edicion.setCupo(cupo);
            edicion.getDocentes().clear();
            for (Docente docente : docentesSeleccionados) {
                Docente gestionado = em.find(Docente.class, docente.getNickname());
                if (gestionado == null) {
                    tx.rollback();
                    return false;
                }
                edicion.getDocentes().add(gestionado);
            }
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    // =====================================================
    // CONSULTA DE INSTITUTOS Y EDICIONES
    // =====================================================

    @WebMethod
    public List<InstitutoDTO> listarInstitutos() {

        Map<String, Instituto> institutos = controladorInstituto.listarInstitutos();
        List<InstitutoDTO> resultado = new ArrayList<>();

        for (Instituto instituto : institutos.values()) {
            resultado.add(new InstitutoDTO(instituto.getNombre()));
        }
        return resultado;
    }

    @WebMethod
    public List<EdicionCursoDTO> listarEdicionesPorInstituto(@WebParam(name = "nombreInstituto") String nombreInstituto) {

        List<EdicionCursoDTO> resultado = new ArrayList<>();
        if (nombreInstituto == null || nombreInstituto.isBlank()) {
            return resultado;
        }
        Instituto instituto = controladorInstituto.buscarInstituto(nombreInstituto);
        if (instituto == null) {
            return resultado;
        }
        Map<String, EdicionCurso> ediciones = controladorCurso.listarEdiciones();
        for (EdicionCurso edicion : ediciones.values()) {
            if (edicion.getCurso() == null || edicion.getCurso().getInstituto() == null) {
                continue;
            }
            String nombreDelInstituto = edicion.getCurso().getInstituto().getNombre();
            if (!nombreInstituto.equals(nombreDelInstituto)) {
                continue;
            }
            EdicionCursoDTO dto = new EdicionCursoDTO(
                            edicion.getNombre(),
                            edicion.getCurso().getNombre(),
                            nombreDelInstituto,
                            edicion.getFechaInicio() != null ? edicion.getFechaInicio().toString() : "",
                            edicion.getFechaFin() != null ? edicion.getFechaFin().toString() : "",
                            edicion.getCupo());
            dto.setImagen(edicion.getImagen());
            resultado.add(dto);
        }
        return resultado;
    }
    
    @WebMethod
    public List<CursoInscripcionDTO> listarCursosConEdicionesPorInstituto(@WebParam(name = "nombreInstituto") String nombreInstituto) {

        List<CursoInscripcionDTO> resultado = new ArrayList<>();
        if (nombreInstituto == null || nombreInstituto.isBlank()) {
            return resultado;
        }
        Instituto instituto = controladorInstituto.buscarInstituto(nombreInstituto);
        if (instituto == null) {
            return resultado;
        }

        Map<String, CursoInscripcionDTO> cursos = new LinkedHashMap<>();
        Map<String, EdicionCurso> ediciones = controladorCurso.listarEdiciones();

        for (EdicionCurso edicion : ediciones.values()) {

            if (edicion.getCurso() == null || edicion.getCurso().getInstituto() == null) {
                continue;
            }
            Curso curso = edicion.getCurso();
            if (!nombreInstituto.equals(curso.getInstituto().getNombre())) {
                continue;
            }
            CursoInscripcionDTO cursoDTO = cursos.get(curso.getNombre());
            if (cursoDTO == null) {
                cursoDTO = new CursoInscripcionDTO(
                        curso.getNombre(),
                        curso.getDescripcion(),
                        curso.getDuracion());

                cursos.put(curso.getNombre(), cursoDTO);
            }

            EdicionCursoDTO edicionDTO = new EdicionCursoDTO(
                    edicion.getNombre(),
                    curso.getNombre(),
                    nombreInstituto,
                    edicion.getFechaInicio() != null ? edicion.getFechaInicio().toString() : "",
                    edicion.getFechaFin() != null ? edicion.getFechaFin().toString() : "",
                    edicion.getCupo());
            edicionDTO.setImagen(edicion.getImagen());
            cursoDTO.getEdiciones().add(edicionDTO);
        }
        resultado.addAll(cursos.values());
        return resultado;
    }
    
    // =====================================================
    // CREACIÓN DE PROGRAMAS DE FORMACIÓN
    // =====================================================

    @WebMethod
    public ResultadoOperacionDTO crearPrograma(
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "descripcion") String descripcion,
            @WebParam(name = "fechaInicio") String fechaInicioTexto,
            @WebParam(name = "fechaFin") String fechaFinTexto,
            @WebParam(name = "imagen") String imagen) {

        if (nombre == null || nombre.isBlank()) {
            return new ResultadoOperacionDTO(false, "Ingrese un nombre para el programa.");
        }
        if (fechaInicioTexto == null || fechaInicioTexto.isBlank() || fechaFinTexto == null || fechaFinTexto.isBlank()) {
            return new ResultadoOperacionDTO(false, "Debe ingresar ambas fechas.");
        }
        LocalDate fechaInicio;
        LocalDate fechaFin;
        try {
            fechaInicio = LocalDate.parse(fechaInicioTexto);
            fechaFin = LocalDate.parse(fechaFinTexto);
        } catch (DateTimeParseException e) {
            return new ResultadoOperacionDTO(false, "Las fechas ingresadas no son válidas.");
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            return new ResultadoOperacionDTO(false,
                    "La fecha de fin debe ser posterior a la fecha de inicio.");
        }
        ProgramaFormacion programa = new ProgramaFormacion(
                nombre.trim(),
                descripcion != null ? descripcion.trim() : "",
                fechaInicio,
                fechaFin);
        if (imagen != null && !imagen.isBlank()) {
            programa.setImagen(imagen);
        }
        boolean creado = controladorPrograma.altaPrograma(programa);
        if (!creado) {
            return new ResultadoOperacionDTO(false,
                    "No se pudo crear el programa. Verifique si el nombre ya existe.");
        }
        return new ResultadoOperacionDTO(true, "Programa creado correctamente.");
    }

    // =====================================================
    // CONSULTA DE PROGRAMAS DE FORMACIÓN
    // =====================================================
    
    @WebMethod
    public List<ProgramaConsultaDTO> listarProgramasConsulta() {

        Map<String, ProgramaFormacion> programas = controladorPrograma.listarProgramas();
        List<ProgramaConsultaDTO> resultado = new ArrayList<>();

        if (programas == null || programas.isEmpty()) {
            return resultado;
        }

        for (ProgramaFormacion programa : programas.values()) {

            ProgramaConsultaDTO programaDTO = new ProgramaConsultaDTO(
                    programa.getNombre(),
                    programa.getDescripcion(),
                    programa.getImagen(),
                    programa.getFechaInicio() != null ? programa.getFechaInicio().toString() : "",
                    programa.getFechaFin() != null ? programa.getFechaFin().toString() : "");

            Set<String> categorias = new LinkedHashSet<>();

            // Cursos asociados al programa
            for (Curso curso : programa.getCursos()) {
                if (curso == null) {
                    continue;
                }
                CursoProgramaDTO cursoDTO = new CursoProgramaDTO(curso.getNombre(), curso.getDescripcion());
                cursoDTO.setImagen(curso.getImagen());
                programaDTO.getCursos().add(cursoDTO);

                // Categorías asociadas a los cursos
                if (curso.getCategorias() != null) {
                    curso.getCategorias().forEach(categoria -> {
                        if (categoria != null && categoria.getNombre() != null) {
                            categorias.add(categoria.getNombre());
                        }
                    });
                }
            }

            programaDTO.setCategorias(new ArrayList<>(categorias));
            resultado.add(programaDTO);
        }
        return resultado;
    }

    // =====================================================
    // CURSOS PARA AGREGAR A PROGRAMAS
    // =====================================================

    @WebMethod
    public List<CursoProgramaDTO> listarCursosParaPrograma() {
        List<CursoProgramaDTO> resultado = new ArrayList<>();
        Map<String, Curso> cursos = controladorCurso.listarCursos();
        if (cursos == null || cursos.isEmpty()) {
            return resultado;
        }
        for (Curso curso : cursos.values()) {
            if (curso == null) {
                continue;
            }
            CursoProgramaDTO dto = new CursoProgramaDTO();
            dto.setNombre(curso.getNombre());
            dto.setDescripcion(curso.getDescripcion());
            dto.setImagen(curso.getImagen());
            resultado.add(dto);
        }
        return resultado;
    }

    // =====================================================
    // AGREGAR CURSO A PROGRAMA
    // =====================================================

    @WebMethod
    public ResultadoOperacionDTO agregarCursoAPrograma(@WebParam(name = "nombrePrograma") String nombrePrograma,@WebParam(name = "nombreCurso") String nombreCurso) {
        if (nombrePrograma == null || nombrePrograma.isBlank() || nombreCurso == null || nombreCurso.isBlank()) {
            return new ResultadoOperacionDTO(false,"Debe seleccionar un programa y un curso.");
        }
        boolean agregado = controladorPrograma.agregarCurso(nombrePrograma,nombreCurso);
        if (!agregado) {
            return new ResultadoOperacionDTO(false,"No se pudo agregar el curso. Verifique que el programa y el curso existan y que el curso no esté ya asociado.");
        }
        return new ResultadoOperacionDTO(true,"El curso se agregó correctamente al programa.");
    }

    // =====================================================
    // LISTADO DE CURSOS
    // =====================================================

    @WebMethod
    public ListadoCursosDTO listarCursosParaWeb(@WebParam(name = "nickname") String nickname) {

        ListadoCursosDTO resultado = new ListadoCursosDTO();
        // Obtener los cursos
        Map<String, Curso> cursosMap = controladorCurso.listarCursos();
        // Obtener el docente, si corresponde
        Docente docente = null;
        if (nickname != null && !nickname.isBlank()) {
            Usuario usuario = controladorUsuario.buscarUsuario(nickname);
            if (usuario instanceof Docente) {
                docente = (Docente) usuario;
            }
        }
        Set<String> cursosEditables = new LinkedHashSet<>();
        if (cursosMap == null || cursosMap.isEmpty()) {
            return resultado;
        }
        for (Curso curso : cursosMap.values()) {
            if (curso == null) {
                continue;
            }
            CursoListadoDTO cursoDTO = new CursoListadoDTO();

            cursoDTO.setNombre(curso.getNombre());
            cursoDTO.setDescripcion(curso.getDescripcion());
            cursoDTO.setDuracion(curso.getDuracion());
            cursoDTO.setCantHoras(curso.getCantHoras());
            cursoDTO.setCantCreditos(curso.getCantCreditos());
            cursoDTO.setUrl(curso.getUrl());

            if (curso.getInstituto() != null) {

                String nombreInstituto = curso.getInstituto().getNombre();
                cursoDTO.setInstituto(nombreInstituto);
                // Comprobar si el docente pertenece al instituto del curso
                if (docente != null && docente.getInstitutos() != null) {

                    boolean perteneceAlInstituto = docente.getInstitutos()
                            .stream()
                            .filter(instituto -> instituto != null)
                            .anyMatch(instituto -> nombreInstituto.equals(instituto.getNombre()));
                    if (perteneceAlInstituto) {
                        cursosEditables.add(curso.getNombre());
                    }
                }
            }
            resultado.getCursos().add(cursoDTO);
        }
        resultado.setCursosEditables(new ArrayList<>(cursosEditables));
        return resultado;
    }

    // =====================================================
    // INSCRIPCIONES A EDICIONES DE CURSOS
    // =====================================================

    @WebMethod
    public ResultadoOperacionDTO inscribirEstudiante(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreInstituto") String nombreInstituto,
            @WebParam(name = "nombreEdicion") String nombreEdicion) {

        if (nickname == null || nickname.isBlank()
                || nombreInstituto == null || nombreInstituto.isBlank()
                || nombreEdicion == null || nombreEdicion.isBlank()) {

            return new ResultadoOperacionDTO(false,"Faltan datos para realizar la inscripción.");
        }
        Usuario usuario = controladorUsuario.buscarUsuario(nickname);
        if (!(usuario instanceof Estudiante)) {
            return new ResultadoOperacionDTO(false,"Solo los estudiantes pueden inscribirse a ediciones de cursos.");
        }

        Map<String, EdicionCurso> ediciones = controladorCurso.listarEdiciones();
        EdicionCurso edicion = ediciones.get(nombreEdicion);

        if (edicion == null || edicion.getCurso() == null
                || edicion.getCurso().getInstituto() == null 
                || !nombreInstituto.equals(edicion.getCurso().getInstituto().getNombre())) {

            return new ResultadoOperacionDTO(false,"No se encontró la edición seleccionada en ese instituto.");
        }
        InscripcionCurso inscripcion = new InscripcionCurso((Estudiante) usuario,LocalDate.now(),edicion);
        boolean resultado = controladorCurso.inscribirEstudiante(inscripcion);

        if (resultado) {
            return new ResultadoOperacionDTO(true,
                    "Te inscribiste correctamente a la edición "
                    + nombreEdicion + ".");
        }
        return new ResultadoOperacionDTO(false,
                "No se pudo completar la inscripción. "
                + "Es posible que no haya cupos disponibles, "
                + "que ya estés inscrito o que haya ocurrido un error.");
    }
    
    // =====================================================
    // INSCRIPCIONES A PROGRAMAS 
    // =====================================================
    
    @WebMethod
    public ResultadoOperacionDTO inscribirEstudiantePrograma(@WebParam(name = "nickname") String nickname,@WebParam(name = "nombrePrograma") String nombrePrograma) {
        if (nickname == null || nickname.isBlank()) {
            return new ResultadoOperacionDTO(false, "No se especificó el usuario.");
        }
        if (nombrePrograma == null || nombrePrograma.isBlank()) {
            return new ResultadoOperacionDTO(false, "No se especificó el programa.");
        }
        Usuario usuario = controladorUsuario.buscarUsuario(nickname);
        if (usuario == null) {
            return new ResultadoOperacionDTO(false, "El usuario no existe.");
        }
        if (!(usuario instanceof Estudiante)) {
            return new ResultadoOperacionDTO(false, "Solo los estudiantes pueden inscribirse a programas.");
        }

        Estudiante estudiante = (Estudiante) usuario;
        ProgramaFormacion programa = controladorPrograma.buscarPrograma(nombrePrograma);
        if (programa == null) {
            return new ResultadoOperacionDTO(false, "El programa seleccionado no existe.");
        }

        InscripcionPrograma existente = controladorPrograma.buscarInscripcionPrograma(nickname, nombrePrograma);

        if (existente != null) {
            return new ResultadoOperacionDTO(false, "Ya estás inscrito en este programa.");
        }

        InscripcionPrograma inscripcion = new InscripcionPrograma(estudiante,LocalDate.now(),programa);
        boolean resultado = controladorPrograma.inscribirEstudiantePrograma(inscripcion);

        if (resultado) {
            return new ResultadoOperacionDTO(true,
                    "Te inscribiste correctamente al programa "
                    + programa.getNombre() + ".");
        }
        return new ResultadoOperacionDTO(false,
                "No se pudo completar la inscripción. Intentá nuevamente.");
    }

    // ========================
    // SELECCIONAR ESTUDIANTES 
    // ========================

    @WebMethod
    public List<InstitutoDTO> listarInstitutosDocente(@WebParam(name = "nickname") String nickname) {
        List<InstitutoDTO> resultado = new ArrayList<>();
        if (nickname == null || nickname.isBlank()) {
            return resultado;
        }
        EntityManager em = Controladores.Conexion.getInstancia().getEntityManager();

        try {
            List<Instituto> institutos = em.createQuery(
                    "SELECT i FROM Docente d JOIN d.institutos i "
                    + "WHERE d.nickname = :nickname ORDER BY i.nombre",
                    Instituto.class).setParameter("nickname", nickname).getResultList();
            for (Instituto instituto : institutos) {
                resultado.add(new InstitutoDTO(instituto.getNombre()));
            }
        } finally {
            em.close();
        }
        return resultado;
    }

    @WebMethod
    public List<CursoProgramaDTO> listarCursosDocente(@WebParam(name = "nickname") String nickname,@WebParam(name = "nombreInstituto") String nombreInstituto) {
        List<CursoProgramaDTO> resultado = new ArrayList<>();
        if (nickname == null || nickname.isBlank() || nombreInstituto == null || nombreInstituto.isBlank()) {
            return resultado;
        }
        EntityManager em = Controladores.Conexion.getInstancia().getEntityManager();
        try {
            List<Curso> cursos = em.createQuery(
                    "SELECT c FROM Curso c "
                    + "JOIN c.instituto i "
                    + "JOIN i.docentes d "
                    + "WHERE i.nombre = :instituto "
                    + "AND d.nickname = :nickname "
                    + "ORDER BY c.nombre",
                    Curso.class)
                    .setParameter("instituto", nombreInstituto)
                    .setParameter("nickname", nickname)
                    .getResultList();
            for (Curso curso : cursos) {
                CursoProgramaDTO dto = new CursoProgramaDTO();
                dto.setNombre(curso.getNombre());
                dto.setDescripcion(curso.getDescripcion());
                dto.setImagen(curso.getImagen());
                resultado.add(dto);
            }
        } finally {
            em.close();
        }
        return resultado;
    }

    @WebMethod
    public List<EdicionCursoDTO> listarEdicionesDocente(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreInstituto") String nombreInstituto,
            @WebParam(name = "nombreCurso") String nombreCurso) {

        List<EdicionCursoDTO> resultado = new ArrayList<>();
        if (nickname == null || nickname.isBlank()
                || nombreInstituto == null || nombreInstituto.isBlank()
                || nombreCurso == null || nombreCurso.isBlank()) {
            return resultado;
        }
        EntityManager em = Controladores.Conexion.getInstancia().getEntityManager();
        try {
            List<EdicionCurso> ediciones = em.createQuery(
                    "SELECT e FROM EdicionCurso e "
                    + "JOIN FETCH e.curso c "
                    + "JOIN c.instituto i "
                    + "JOIN e.docentes d "
                    + "WHERE c.nombre = :curso "
                    + "AND i.nombre = :instituto "
                    + "AND d.nickname = :nickname "
                    + "ORDER BY e.fechaInicio",
                    EdicionCurso.class)
                    .setParameter("curso", nombreCurso)
                    .setParameter("instituto", nombreInstituto)
                    .setParameter("nickname", nickname)
                    .getResultList();

            for (EdicionCurso edicion : ediciones) {
                EdicionCursoDTO dto = new EdicionCursoDTO();
                dto.setNombre(edicion.getNombre());
                dto.setNombreCurso(edicion.getCurso().getNombre());
                dto.setNombreInstituto(nombreInstituto);
                dto.setFechaInicio(edicion.getFechaInicio() != null ? edicion.getFechaInicio().toString() : null);
                dto.setFechaFin(edicion.getFechaFin() != null ? edicion.getFechaFin().toString() : null);
                dto.setCupo(edicion.getCupo());
                dto.setCerrada(edicion.isCerrada());
                dto.setImagen(edicion.getImagen());
                resultado.add(dto);
            }
        } finally {
            em.close();
        }
        return resultado;
    }

    @WebMethod
    public List<InscripcionSeleccionDTO> listarInscripcionesEdicionDocente(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreInstituto") String nombreInstituto,
            @WebParam(name = "nombreCurso") String nombreCurso,
            @WebParam(name = "nombreEdicion") String nombreEdicion) {

        List<InscripcionSeleccionDTO> resultado = new ArrayList<>();

        if (nickname == null || nickname.isBlank()
                || nombreInstituto == null || nombreInstituto.isBlank()
                || nombreCurso == null || nombreCurso.isBlank()
                || nombreEdicion == null || nombreEdicion.isBlank()) {
            return resultado;
        }
        // Primero se comprueba que el docente tenga acceso a esa edición.
        List<EdicionCursoDTO> ediciones = listarEdicionesDocente(nickname, nombreInstituto, nombreCurso);
        boolean edicionPermitida = ediciones.stream().anyMatch(e -> nombreEdicion.equals(e.getNombre()));
        if (!edicionPermitida) {
            return resultado;
        }
        List<InscripcionCurso> inscripciones = controladorCurso.listarInscripcionesEdicion(nombreEdicion);
        if (inscripciones == null) {
            return resultado;
        }
        for (InscripcionCurso inscripcion : inscripciones) {
            if (inscripcion == null) {
                continue;
            }
            UsuarioDTO estudianteDTO = null;
            if (inscripcion.getEstudiante() != null) {
                estudianteDTO = convertirUsuario(inscripcion.getEstudiante());
            }
            InscripcionSeleccionDTO dto = new InscripcionSeleccionDTO(
                    inscripcion.getId(),
                    estudianteDTO,
                    inscripcion.getFechaIns() != null ? inscripcion.getFechaIns().toString() : null,
                    inscripcion.getEstado() != null ? inscripcion.getEstado().name() : null );

            dto.setNota(inscripcion.getNota());
            resultado.add(dto);
        }
        return resultado;
    }

    @WebMethod
    public ResultadoOperacionDTO cambiarEstadoInscripcionDocente(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreInstituto") String nombreInstituto,
            @WebParam(name = "nombreCurso") String nombreCurso,
            @WebParam(name = "nombreEdicion") String nombreEdicion,
            @WebParam(name = "idInscripcion") Long idInscripcion,
            @WebParam(name = "nuevoEstado") String nuevoEstadoTexto) {

        if (nickname == null || nickname.isBlank()
                || nombreInstituto == null || nombreInstituto.isBlank()
                || nombreCurso == null || nombreCurso.isBlank()
                || nombreEdicion == null || nombreEdicion.isBlank()
                || idInscripcion == null
                || nuevoEstadoTexto == null || nuevoEstadoTexto.isBlank()) {
            return new ResultadoOperacionDTO(false, "Faltan datos para gestionar la inscripción.");
        }
        EstadoInscripcion nuevoEstado;
        try {
            nuevoEstado = EstadoInscripcion.valueOf(nuevoEstadoTexto);
        } catch (IllegalArgumentException e) {
            return new ResultadoOperacionDTO(false, "El estado seleccionado no es válido.");
        }
        if (nuevoEstado != EstadoInscripcion.ACEPTADA && nuevoEstado != EstadoInscripcion.RECHAZADA) {
            return new ResultadoOperacionDTO(false, "El estado seleccionado no es válido.");
        }
        // Verificar que el docente participa en la edición indicada.
        List<EdicionCursoDTO> ediciones = listarEdicionesDocente(nickname, nombreInstituto, nombreCurso);
        boolean puedeGestionar = ediciones.stream().anyMatch(e -> nombreEdicion.equals(e.getNombre()));
        if (!puedeGestionar) {
            return new ResultadoOperacionDTO(false, "No puedes gestionar esa edición.");
        }
        // Verificar que la inscripción pertenece a esa edición.
        List<InscripcionCurso> inscripciones = controladorCurso.listarInscripcionesEdicion(nombreEdicion);
        boolean inscripcionPertenece = inscripciones != null && inscripciones.stream().anyMatch(i -> i != null && idInscripcion.equals(i.getId()));

        if (!inscripcionPertenece) {
            return new ResultadoOperacionDTO(false,"La inscripción no pertenece a la edición seleccionada.");
        }
        EdicionCurso edicion = controladorCurso.buscarEdicion(nombreEdicion);
        if (edicion == null || edicion.isCerrada()) {
            return new ResultadoOperacionDTO(false,"No se pueden modificar inscripciones de una edición cerrada.");
        }
        boolean actualizado = controladorCurso.cambiarEstadoInscripcion(idInscripcion, nuevoEstado);
        if (!actualizado) {
            return new ResultadoOperacionDTO(false,"No se pudo actualizar el estado de la inscripción.");
        }
        return new ResultadoOperacionDTO(true, nuevoEstado == EstadoInscripcion.ACEPTADA ? "La inscripción fue aceptada correctamente." : "La inscripción fue rechazada correctamente.");
    }
    
    private boolean docentePuedeGestionarEdicion(String nickname,String nombreInstituto,String nombreCurso,String nombreEdicion) {
        if(nickname == null || nickname.isBlank()
                || nombreInstituto == null || nombreInstituto.isBlank()
                || nombreCurso == null || nombreCurso.isBlank()
                || nombreEdicion == null || nombreEdicion.isBlank()) {
            return false;
        }
        List<EdicionCursoDTO> ediciones = listarEdicionesDocente(nickname, nombreInstituto, nombreCurso);
        return ediciones != null && ediciones.stream().anyMatch(e -> nombreEdicion.equals(e.getNombre()));
    }
    
    @WebMethod
    public ResultadoOperacionDTO guardarEvaluacionesDocente(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreInstituto") String nombreInstituto,
            @WebParam(name = "nombreCurso") String nombreCurso,
            @WebParam(name = "nombreEdicion") String nombreEdicion,
            @WebParam(name = "evaluaciones") List<EvaluacionDTO> evaluaciones) {

        if(!docentePuedeGestionarEdicion(nickname, nombreInstituto, nombreCurso, nombreEdicion)) {
            return new ResultadoOperacionDTO(false,"No tienes permiso para gestionar esta edición.");
        }
        if(evaluaciones == null || evaluaciones.isEmpty()) {
            return new ResultadoOperacionDTO(false,"No se ingresaron evaluaciones.");
        }
        EdicionCurso edicion = controladorCurso.buscarEdicion(nombreEdicion);
        if (edicion == null) {
            return new ResultadoOperacionDTO(false,"No se encontró la edición seleccionada.");
        }
        if(edicion.isCerrada()) {
            return new ResultadoOperacionDTO(false,"La edición está cerrada y no permite modificar evaluaciones.");
        }
        Map<Long, Integer> notas = new LinkedHashMap<>();
        for(EvaluacionDTO evaluacion : evaluaciones) {
            if(evaluacion == null || evaluacion.getIdInscripcion() == null || evaluacion.getNota() == null) {
                return new ResultadoOperacionDTO(false,"Todas las evaluaciones deben tener una nota.");
            }
            Integer nota = evaluacion.getNota();
            if(nota < 1 || nota > 12) {
                return new ResultadoOperacionDTO(false,"Las notas deben estar comprendidas entre 1 y 12.");
            }
            if(notas.containsKey(evaluacion.getIdInscripcion())) {
                return new ResultadoOperacionDTO(false,"Se recibió más de una nota para el mismo estudiante.");
            }
            notas.put(evaluacion.getIdInscripcion(), nota);
        }
        boolean guardado = controladorCurso.guardarNotasEdicion(nombreEdicion, notas);
        if(!guardado) {
            return new ResultadoOperacionDTO(false,"No se pudieron guardar las evaluaciones. Verifica los datos e inténtalo nuevamente.");
        }
        return new ResultadoOperacionDTO(true,"Las evaluaciones fueron guardadas correctamente.");
    }
    
    
    @WebMethod
    public ResultadoOperacionDTO cerrarEdicionDocente(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "nombreInstituto") String nombreInstituto,
            @WebParam(name = "nombreCurso") String nombreCurso,
            @WebParam(name = "nombreEdicion") String nombreEdicion) {

        if(!docentePuedeGestionarEdicion(nickname, nombreInstituto, nombreCurso, nombreEdicion)) {
            return new ResultadoOperacionDTO(false,"No tienes permiso para cerrar esta edición.");
        }
        EdicionCurso edicion = controladorCurso.buscarEdicion(nombreEdicion);
        if(edicion == null) {
            return new ResultadoOperacionDTO(false,"No se encontró la edición seleccionada.");
        }
        if(edicion.isCerrada()) {
            return new ResultadoOperacionDTO(false,"La edición ya se encuentra cerrada.");
        }
        List<InscripcionCurso> inscripciones = controladorCurso.listarInscripcionesEdicion(nombreEdicion);
        if(inscripciones != null) {
            for(InscripcionCurso inscripcion : inscripciones) {
                if(inscripcion == null || inscripcion.getEstado() != EstadoInscripcion.ACEPTADA) {
                    continue;
                }
                Integer nota = inscripcion.getNota();
                if(nota == null || nota < 1 || nota > 12) {
                    return new ResultadoOperacionDTO(false,"No se puede cerrar la edición: todavía hay estudiantes sin evaluar.");
                }
            }
        }
        boolean cerrada = controladorCurso.cerrarEdicion(nombreEdicion);
        if (!cerrada) {
            return new ResultadoOperacionDTO(false,"No se pudo cerrar la edición.");
        }
        return new ResultadoOperacionDTO(true,"La edición fue cerrada correctamente. Las evaluaciones ya no podrán modificarse.");
    }

    
    // =====================================================
    // RESULTADO INSCRIPCIONES 
    // =====================================================

    @WebMethod
    public List<InscripcionCursoDTO> listarInscripcionesEstudiante(@WebParam(name = "nickname") String nickname) {
        List<InscripcionCursoDTO> resultado = new ArrayList<>();
        if (nickname == null || nickname.isBlank()) {
            return resultado;
        }
        Usuario usuario = controladorUsuario.buscarUsuario(nickname);
        if (!(usuario instanceof Estudiante)) {
            return resultado;
        }
        List<InscripcionCurso> inscripciones = controladorCurso.listarInscripcionesEstudiante(nickname);
        if (inscripciones == null) {
            return resultado;
        }
        for (InscripcionCurso inscripcion : inscripciones) {

            if (inscripcion == null) {
                continue;
            }
            InscripcionCursoDTO dto = new InscripcionCursoDTO();
            EdicionCurso edicion = inscripcion.getCurso();
            dto.setNombreEdicion(edicion != null ? edicion.getNombre() : null);
            dto.setNombreCurso(edicion != null && edicion.getCurso() != null ? edicion.getCurso().getNombre() : null);
            dto.setFechaInscripcion(inscripcion.getFechaIns() != null ? inscripcion.getFechaIns().toString() : null);
            dto.setEstado(inscripcion.getEstado() != null ? inscripcion.getEstado().toString() : null);
            resultado.add(dto);
        }
        return resultado;
    }
    
    @WebMethod
    public List<InscripcionCursoDTO> verResultadosEvaluacionesEstudiante(@WebParam(name = "nickname") String nickname) {
        List<InscripcionCursoDTO> resultado = new ArrayList<>();
        if(nickname == null || nickname.isBlank()) {
            return resultado;
        }
        Usuario usuario = controladorUsuario.buscarUsuario(nickname);
        if(!(usuario instanceof Estudiante)) {
            return resultado;
        }
        List<InscripcionCurso> inscripciones = controladorCurso.listarInscripcionesEstudiante(nickname);
        if(inscripciones == null) {
            return resultado;
        }
        for(InscripcionCurso inscripcion : inscripciones) {
            if(inscripcion == null) {
                continue;
            }
            EdicionCurso edicion = inscripcion.getCurso();
            if(edicion == null || !edicion.isCerrada()) {
                continue;
            }
            InscripcionCursoDTO dto = new InscripcionCursoDTO();
            dto.setNombreEdicion(edicion.getNombre());
            dto.setNombreCurso(edicion.getCurso() != null ? edicion.getCurso().getNombre() : null);
            dto.setFechaInscripcion(inscripcion.getFechaIns() != null ? inscripcion.getFechaIns().toString(): null);
            dto.setEstado(inscripcion.getEstado() != null ? inscripcion.getEstado().toString() : null);
            dto.setNota(inscripcion.getNota() != null ? inscripcion.getNota().toString() : null);
            resultado.add(dto);
        }
        return resultado;
    }

    // ==================== BUSCADOR ====================

    @WebMethod
    public List<ResultadoBusquedaDTO> buscar(
            @WebParam(name = "query") String query,
            @WebParam(name = "filtro") String filtro,
            @WebParam(name = "orden") String orden) {

        List<ResultadoBusquedaDTO> resultadosDTO = new ArrayList<>();

        List<Datatypes.ResultadoBusqueda> resultados = buscador.buscar(query, filtro, orden);

        if (resultados == null) {
            return resultadosDTO;
        }

        for (Datatypes.ResultadoBusqueda resultado : resultados) {

            ResultadoBusquedaDTO dto = new ResultadoBusquedaDTO();

            dto.setTipo(resultado.getTipo());
            dto.setId(resultado.getId());
            dto.setNombre(resultado.getNombre());
            dto.setDescripcion(resultado.getDescripcion());

            if (resultado.getFecha() != null) {
                dto.setFecha(resultado.getFecha().toString());
            }

            dto.setImagen(resultado.getImagen());
            resultadosDTO.add(dto);
        }
        return resultadosDTO;
    }

    // =====================================================
    // IMÁGENES (carpeta local del servidor central)
    // =====================================================
    
    @WebMethod
    public byte[] obtenerImagen(@WebParam(name = "ruta") String ruta) {
        if (ruta == null || ruta.isBlank()) return null;
        Path raiz = Paths.get(Configuracion.get("imagenes.ruta")).toAbsolutePath().normalize();
        Path archivo = raiz.resolve(ruta).normalize();
        if (!archivo.startsWith(raiz) || !Files.isRegularFile(archivo)) return null;
        try {
            return Files.readAllBytes(archivo);
        } catch (IOException e) {
            return null;
        }
    }

    @WebMethod
    public boolean guardarImagenUsuario(
            @WebParam(name = "nickname") String nickname,
            @WebParam(name = "extension") String extension,
            @WebParam(name = "contenido") byte[] contenido) {

        if (nickname == null || nickname.isBlank()
                || nickname.contains("/") || nickname.contains("\\")
                || nickname.equals(".") || nickname.equals("..")) return false;
        if (extension == null) return false;
        extension = extension.toLowerCase();
        if (!extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".png")) return false;
        if (contenido == null || contenido.length == 0 || contenido.length > 5 * 1024 * 1024) return false;

        EntityManager em = Conexion.getInstancia().getEntityManager();
        try {
            Usuario u = em.find(Usuario.class, nickname);
            if (u == null) return false;

            Path carpeta = Paths.get(Configuracion.get("imagenes.ruta"), "imagenes");
            Files.createDirectories(carpeta);
            Files.write(carpeta.resolve(nickname + extension), contenido);

            // Borra versiones anteriores con otra extensión
            for (String ext : new String[]{".jpg", ".jpeg", ".png"}) {
                if (!ext.equals(extension)) Files.deleteIfExists(carpeta.resolve(nickname + ext));
            }

            em.getTransaction().begin();
            u.setImagen("imagenes/" + nickname + extension);
            em.getTransaction().commit();
            return true;
        } catch (IOException | RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

}