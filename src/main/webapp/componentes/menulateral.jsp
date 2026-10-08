<%-- 
    Document   : menulateral
    Created on : Sep 25, 2026, 5:35:54?PM
    Author     : adolfo
--%>

<aside class="bg-dark border-end p-1" style="width: 250px; min-height: calc(100vh - 70px);">
    <hr class="border border-light border-1 opacity-100">
    <!-- Institutos -->
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">Institutos</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Instituto Tecnológico</a></li>
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Instituto de Computación</a></li>
        </ul>
    </div>
    <hr class="border border-light border-1 opacity-100">
    <!-- Categorías -->
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">Categorías</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Programación</a></li>
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Matemática</a></li>
            <li class="mb-1"><a href="#" class="text-decoration-none text-white">Informática</a></li>
        </ul>
    </div>
        <hr class="border border-light border-1 opacity-100">
    <!-- Curso -->
    <div>
        <h6 class="text-uppercase text-secondary fw-bold text-white">Menu Opciones <br>(segun tipo usuario)</h6>
        <ul class="list-unstyled">
            <li class="mb-1"><a href="principal.jsp?contenido=altaCurso" class="text-decoration-none text-white">Alta Curso</a></li>
            <li class="mb-1"><a href="principal.jsp?contenido=listarCursos" class="text-decoration-none text-white">Listar Curso</a></li>
            <li class="mb-1"><a href="<%= request.getContextPath() %>/ConsultaCurso?nombre=Dalavuelta">Ver Curso Programación</a></li>
        </ul>
    </div>
</aside>
