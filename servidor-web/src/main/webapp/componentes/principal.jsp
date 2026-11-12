<%-- 
    Document   : principal
    Created on : Sep 27, 2026, 9:43:52 PM
    Author     : adolfo
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="WebServices.ClienteSOAP"%>
<%@page import="com.mycompany.edextweb.soap.CursoListadoDTO"%>
<%@page import="com.mycompany.edextweb.soap.CursoTendenciaDTO"%>
<%@page import="com.mycompany.edextweb.soap.EdextWebService"%>
<%@page import="com.mycompany.edextweb.soap.ListadoCursosDTO"%>
<%@page import="com.mycompany.edextweb.soap.ResultadoBusquedaDTO"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%
    // Los 3 cursos más visitados, con su imagen y descripción.
    List<Map<String, String>> masVisitados = new ArrayList<>();
    try {
        EdextWebService servicio = ClienteSOAP.obtenerServicio();

        List<CursoTendenciaDTO> todos = new ArrayList<>(servicio.listarCursosMasVisitados());
        todos.removeIf(t -> t.getVisitas() <= 0);
        todos.sort(Comparator.comparingInt(CursoTendenciaDTO::getVisitas).reversed());
        List<CursoTendenciaDTO> top = todos.subList(0, Math.min(3, todos.size()));

        // Descripciones de los cursos.
        Map<String, String> descripciones = new HashMap<>();
        ListadoCursosDTO listado = servicio.listarCursosParaWeb(null);
        if (listado != null) {
            for (CursoListadoDTO curso : listado.getCursos()) {
                descripciones.put(curso.getNombre(), curso.getDescripcion());
            }
        }

        for (CursoTendenciaDTO t : top) {
            String nombre = t.getNombre();

            // La imagen se toma del resultado de búsqueda del curso (misma ruta que usa el buscador).
            String imagen = "";
            try {
                for (ResultadoBusquedaDTO r : servicio.buscar(nombre, "", "")) {
                    if ("curso".equalsIgnoreCase(r.getTipo()) && nombre.equals(r.getNombre())
                            && r.getImagen() != null && !r.getImagen().isBlank()) {
                        imagen = request.getContextPath() + "/imagen?ruta="
                                + URLEncoder.encode(r.getImagen(), "UTF-8");
                        break;
                    }
                }
            } catch (jakarta.xml.ws.WebServiceException ex) {
                application.log("No se pudo obtener la imagen del curso " + nombre, ex);
            }

            // Descripción recortada para la portada.
            String descripcion = descripciones.get(nombre);
            if (descripcion == null) {
                descripcion = "";
            }
            if (descripcion.length() > 220) {
                int corte = descripcion.lastIndexOf(' ', 220);
                descripcion = descripcion.substring(0, corte > 0 ? corte : 220) + "...";
            }

            Map<String, String> item = new HashMap<>();
            item.put("nombre", nombre);
            item.put("descripcion", descripcion);
            item.put("imagen", imagen);
            item.put("url", request.getContextPath() + "/SvltConsultarCurso?nombre="
                    + URLEncoder.encode(nombre, "UTF-8"));
            masVisitados.add(item);
        }
    } catch (jakarta.xml.ws.WebServiceException ex) {
        application.log("Error al cargar los cursos más visitados de la página principal", ex);
    }
    request.setAttribute("masVisitados", masVisitados);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - Plataforma de Educación Virtual</title>
    <!-- Bootstrap 5 CSS // Actualizo a ultima version @adolfo -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Iconos de Bootstrap // Actualizo a ulitma version @adolfo -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
   <style>
    .avatar-sm {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        object-fit: cover;
        font-size: 1.4rem;
        flex-shrink: 0;
    }

    .avatar-lg {
        width: 80px;
        height: 80px;
        border-radius: 50%;
        object-fit: cover;
    }

    .course-card-img {
        height: 180px;
        object-fit: cover;
    }

    .portada-curso-img {
        width: 150px;
        height: 100px;
        object-fit: cover;
        flex-shrink: 0;
    }

    .portada-curso-vacio {
        width: 150px;
        height: 100px;
        flex-shrink: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #e9ecef;
        color: #6c757d;
        font-size: 2rem;
    }
</style>
</head>

<body>
    <!-- Encabezado reutilizable -->
    <jsp:include page="header.jsp" />
    <!-- Contenido principal -->
    <div class="d-flex">
        <!-- Menu lateral reutilizable -->
        <jsp:include page="menulateral.jsp" />
        <main class="container bg-light">
            <h1>Bienvenido a edExt</h1>

            <div class="mt-4">
                <c:forEach var="curso" items="${masVisitados}">
                    <div class="d-flex gap-3 mb-4">
                        <c:choose>
                            <c:when test="${not empty curso.imagen}">
                                <img class="portada-curso-img" src="<c:out value='${curso.imagen}'/>"
                                     alt="Imagen del curso <c:out value='${curso.nombre}'/>">
                            </c:when>
                            <c:otherwise>
                                <div class="portada-curso-vacio" aria-hidden="true">
                                    <i class="bi bi-mortarboard-fill"></i>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <p class="mb-0">
                            <c:out value="${curso.descripcion}"/>
                            <a href="<c:out value='${curso.url}'/>">Leer más</a>
                        </p>
                    </div>
                </c:forEach>
            </div>
        </main>
    </div>
    <!-- JavaScript de Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>