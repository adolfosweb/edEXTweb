<%@ page import="com.mycompany.edextweb.soap.UsuarioDTO" %>
<%@ page import="com.mycompany.edextweb.soap.InstitutoDTO" %>
<%@ page import="WebServices.ClienteSOAP" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    UsuarioDTO usuario =(UsuarioDTO) session.getAttribute("usuario");
    List<InstitutoDTO> institutosMenu = new java.util.ArrayList<>();
    try {
        institutosMenu = ClienteSOAP.obtenerServicio().listarInstitutos();
    } catch (jakarta.xml.ws.WebServiceException ex) {
        application.log("Error al cargar institutos del menú lateral", ex);
    }

    request.setAttribute("institutosMenu", institutosMenu);
%>
<aside class="bg-dark border-end p-1" style="width: 250px; min-height: calc(100vh - 70px);">
    <hr class="border border-light border-1 opacity-100">
    <%
        if (usuario != null && "ESTUDIANTE".equals(usuario.getTipoUsuario())) {
    %>
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white text-decoration-none">
            Inscripciones
        </h6>
        <ul class="list-unstyled">
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/inscripcionEdicion"
                   class="text-decoration-none text-white">Inscribirme a curso</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/inscripcionPrograma"
                   class="text-decoration-none text-white">Inscribirme a programa</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/resultadosInscripciones"
                   class="text-decoration-none text-white">Resultados Inscripciones</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/verResultadosEvaluacionesEstudiante"
                   class="text-decoration-none text-white">Resultados Evaluaciones</a>
            </li>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
    <%
        } else if (usuario != null && "DOCENTE".equals(usuario.getTipoUsuario())) {
    %>
    <div>
        <ul class="list-unstyled">
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/SvltListarCursos"
                   class="text-uppercase text-secondary fw-bold text-white text-decoration-none">Cursos</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/AltaCurso"
                   class="text-decoration-none text-white">Alta curso</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/SvltListarCursos"
                   class="text-decoration-none text-white">Alta edicion</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/crearPrograma"
                   class="text-decoration-none text-white">Alta programa</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/agregarCursoAPrograma"
                   class="text-decoration-none text-white">Agregar curso a programa</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/seleccionarEstudiantes"
                   class="text-decoration-none text-white">Selección estudiantes</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/ingresarEvaluaciones"
                   class="text-decoration-none text-white">Ingresar Evaluaciones</a>
            </li>
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/verResultadosEvaluaciones"
                   class="text-decoration-none text-white">Resultados Evaluaciones</a>
            </li>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
    <%
        }
    %>
    <!-- Institutos -->
    <div>
        <h6 class="text-uppercase fw-bold text-white">Institutos</h6>
        <ul class="list-unstyled">
            <c:choose>
                <c:when test="${not empty institutosMenu}">
                    <c:forEach var="instituto" items="${institutosMenu}">
                        <li class="mb-1">
                            <span class="text-decoration-none text-white">
                                <c:out value="${instituto.nombre}" />
                            </span>
                        </li>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <li class="text-secondary">No hay institutos disponibles.</li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
    <!-- Categorias -->
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">Categorias</h6>
        <ul class="list-unstyled">
            <li class="mb-1">
                <a href="#" class="text-decoration-none text-white">Programacion</a>
            </li>
            <li class="mb-1">
                <a href="#" class="text-decoration-none text-white">Matematica</a>
            </li>
            <li class="mb-1">
                <a href="#" class="text-decoration-none text-white">Informatica</a>
            </li>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
    <%
        if (usuario != null) {
    %>
    <div>
        <ul class="list-unstyled">
            <li class="mb-1">
                <a href="${pageContext.request.contextPath}/LogoutServlet"
                   class="text-uppercase text-secondary fw-bold text-white text-decoration-none">
                    <i class="bi bi-box-arrow-right me-2"></i>Salir
                </a>
            </li>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
    <%
        }
    %>
</aside>