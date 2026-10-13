<%@page import="Logica.Docente"%>
<%@page import="Logica.Usuario"%>
<%-- 
    Document   : menulateral
    Created on : Sep 25, 2026, 5:35:54?PM
    Author     : adolfo
--%>
<%
    Usuario usuarioMenu = (Usuario) session.getAttribute("usuario");
%>

<aside class="bg-dark border-end p-1" style="width: 250px; min-height: calc(100vh - 70px);">
    <% if (usuarioMenu != null) { %>
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">MI PERFIL</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="${pageContext.request.contextPath}/ModificarPerfil" class="text-decoration-none text-white">Modificar...</a></li>
        </ul>
    </div>
    <% } %>
    <hr class="border border-light border-1 opacity-100">
    <!-- Institutos -->
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">Institutos</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Instituto Tecnol�gico</a></li>
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Instituto de Computaci�n</a></li>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
    <!-- Categor�as -->
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">Categor�as</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Programaci�n</a></li>
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Matem�tica</a></li>
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Inform�tica</a></li>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
    <!--Parte para probar llamando desde principal (Se elimina) -->
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">Pruebas</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="crearPrograma.jsp" class="text-decoration-none text-white">Crear Programa</a></li>
            <li class="mb-1"><a href="${pageContext.request.contextPath}/SvltListarCursos" class="text-decoration-none text-white">Listar Cursos</a></li>
        </ul>
    </div>
    <% if (usuarioMenu instanceof Docente) { %>
    <hr class="border border-light border-1 opacity-100">
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">DOCENTE</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="${pageContext.request.contextPath}/AltaCurso" class="text-decoration-none text-white">Alta de Curso</a></li>
        </ul>
    </div>
    <% } %>
</aside>
