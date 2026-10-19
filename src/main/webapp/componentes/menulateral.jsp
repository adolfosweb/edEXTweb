<%@page import="Logica.Docente"%>
<%@page import="Logica.Estudiante"%>
<%@page import="Logica.Usuario"%>
<%@ page import="Controladores.ControladorInstituto" %>
<%@ page import="Logica.Instituto" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    ControladorInstituto controladorInstituto = new ControladorInstituto();
    Map<String, Instituto> institutos = controladorInstituto.listarInstitutos();
    Usuario usuario = (Logica.Usuario) session.getAttribute("usuario");
    request.setAttribute("institutos", institutos);
%>
<aside class="bg-dark border-end p-1" style="width: 250px; min-height: calc(100vh - 70px);">
    <% 
    if (usuario != null) 
    { %>
    <hr class="border border-light border-1 opacity-100">
    <div>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="<%= request.getContextPath() %>/PerfilUsuarioServlet?nickname=<%= java.net.URLEncoder.encode(usuario.getNickname(), "UTF-8") %>" 
                                class="text-uppercase text-secondary fw-bold text-white">Mi Perfil</a></li>
        </ul>
    </div>
    <% } %>
    <hr class="border border-light border-1 opacity-100">
    <%
    if(usuario instanceof Estudiante){
    %>
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">Inscripciones</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="${pageContext.request.contextPath}/inscripcionEdicion" class="text-decoration-none text-white">Inscribirme a curso</a></li>
            <li class="mb-1"><a href="${pageContext.request.contextPath}/inscripcionPrograma" class="text-decoration-none text-white">Inscribirme a programa</a></li>
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Resultados</a></li>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
    <%
    } else if (usuario instanceof Docente) {
    %>
    <div>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="${pageContext.request.contextPath}/SvltListarCursos" class="text-uppercase text-secondary fw-bold text-white">Cursos</a></li>
            <li class="mb-1"><a href="${pageContext.request.contextPath}/AltaCurso" class="text-decoration-none text-white">Alta curso</a></li>
            <li class="mb-1"><a href="${pageContext.request.contextPath}/SvltListarCursos" class="text-decoration-none text-white">Alta edicion</a></li><!--$ {pageContext.request.contextPath}/SvltAltaEdicionCurso-->
            <li class="mb-1"><a href="${pageContext.request.contextPath}/crearPrograma" class="text-decoration-none text-white">Alta programa</a></li>
            <li class="mb-1"><a href="${pageContext.request.contextPath}/agregarCursoAPrograma" class="text-decoration-none text-white">Agregar curso a programa</a></li>
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
                <c:when test="${not empty institutos}">
                    <c:forEach var="entrada" items="${institutos}">
                        <li class="mb-1">
                            <a href="#" class="text-decoration-none text-white"><c:out value="${entrada.key}" /></a>
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
    <!-- Categorias  -->
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">Categorias</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Programacion</a></li>
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Matematica</a></li>
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Informatica</a></li>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
</aside>
