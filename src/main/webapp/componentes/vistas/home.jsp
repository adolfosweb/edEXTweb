<%-- 
    Document   : home.jsp
    Created on : Oct 6, 2026, 10:44:40 PM
    Author     : adolfo
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>

<%--
    Fragmento Vista Home para edEXT
    Despliega las novedades y proyectos destacados en la parte central del sitio web (Req. 7.1)
--%>

<!-- BANNER / BIENVENIDA -->
<div class="bg-primary text-white p-4 rounded-3 mb-4 shadow-sm">
    <h1 class="display-5 fw-bold">Bienvenido a edEXT</h1>
    <p class="fs-5">Plataforma educativa y social para la gestión de cursos de extensión universitaria.</p>
</div>

<!-- CONTENIDO DESTACADO / NOVEDADES -->
<h3 class="mb-3 border-bottom pb-2 fw-bold text-secondary">Proyectos y Cursos Destacados</h3>

<div class="row row-cols-1 g-4">
    
    <!-- Item 1: Robot Butiá -->
    <div class="col">
        <div class="card h-100 shadow-sm">
            <div class="row g-0 align-items-center">
                <div class="col-md-4 p-3 text-center">
                    <img src="https://via.placeholder.com/300x200?text=Robot+Buti%C3%A1" class="img-fluid rounded-start alt="Robot Butiá">
                </div>
                <div class="col-md-8">
                    <div class="card-body">
                        <h5 class="card-title fw-bold text-primary">Taller de Robótica Educativa - Robot Butiá</h5>
                        <p class="card-text text-muted">
                            La segunda etapa consiste en que trabajen en grupo sobre el diseño e implementación de una experiencia didáctica de inclusión del robot Butiá en el aula, utilizando los conocimientos aprendidos en clase.
                        </p>
                        <a href="ConsultaCurso?id=butia" class="btn btn-outline-primary btn-sm">Leer más &raquo;</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Item 2: Dalavuelta -->
    <div class="col">
        <div class="card h-100 shadow-sm">
            <div class="row g-0 align-items-center">
                <div class="col-md-4 p-3 text-center">
                    <img src="https://via.placeholder.com/300x200?text=Proyecto+Dalavuelta" class="img-fluid rounded-start" alt="Dalavuelta">
                </div>
                <div class="col-md-8">
                    <div class="card-body">
                        <h5 class="card-title fw-bold text-primary">Proyecto Dalavuelta</h5>
                        <p class="card-text text-muted">
                            Dalavuelta es un proyecto de extensión que nace en el Instituto de Ingeniería Mecánica y Producción Industrial (IIMPI) de Fing, que si bien inicia su trabajo en el desarrollo de bicicletas accesibles para...
                        </p>
                        <a href="ConsultaCurso?id=dalavuelta" class="btn btn-outline-primary btn-sm">Leer más &raquo;</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Item 3: Flor del Ceibo -->
    <div class="col">
        <div class="card h-100 shadow-sm">
            <div class="row g-0 align-items-center">
                <div class="col-md-4 p-3 text-center">
                    <img src="https://via.placeholder.com/300x200?text=Flor+del+Ceibo" class="img-fluid rounded-start" alt="Flor del Ceibo">
                </div>
                <div class="col-md-8">
                    <div class="card-body">
                        <h5 class="card-title fw-bold text-primary">Flor del Ceibo</h5>
                        <p class="card-text text-muted">
                            Flor del Ceibo es un proyecto central de la Universidad de la República, que tiene como misión movilizar la participación de estudiantes universitarios en diversos entornos locales.
                        </p>
                        <a href="ConsultaCurso?id=flordelceibo" class="btn btn-outline-primary btn-sm">Leer más &raquo;</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
