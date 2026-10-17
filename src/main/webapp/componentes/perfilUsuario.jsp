
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Logica.Usuario"%>
<%@page import="Logica.Estudiante"%>
<%@page import="Logica.Docente"%>
<%@page import="Logica.InscripcionCurso"%>
<%@page import="Logica.InscripcionPrograma"%>
<%@page import="Logica.EdicionCurso"%>
<%@page import="Logica.EstadoInscripcion"%>
<%@page import="Logica.ProgramaFormacion"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.io.File"%>

<%
    // Usuario cuyo perfil se está consultando
    Usuario usuario = (Usuario) request.getAttribute("usuario");

    // Determina si el perfil pertenece al usuario de la sesión
    boolean esPropioPerfil = Boolean.TRUE.equals(
            request.getAttribute("esPropioPerfil")
    );

    String nickname = usuario != null && usuario.getNickname() != null
            ? usuario.getNickname() : "";

    String nombre = usuario != null && usuario.getNombre() != null
            ? usuario.getNombre() : "";

    String apellido = usuario != null && usuario.getApellido() != null
            ? usuario.getApellido() : "";

    String correo = usuario != null && usuario.getCorreoElectronico() != null
            ? usuario.getCorreoElectronico() : "";

    String fechaNacimiento = "";

    if (usuario != null && usuario.getFecNac() != null) {
        fechaNacimiento = usuario.getFecNac().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
    }

    // Buscar la foto existente en imagenesUsuarios
    String extensionFoto = "";
    String directorioFotos = application.getRealPath("/imagenesUsuarios");

    if (directorioFotos != null && !nickname.isEmpty()) {
        String[] extensiones = {".jpg", ".jpeg", ".png"};

        for (String extension : extensiones) {
            File archivo = new File(
                    directorioFotos,
                    nickname + extension
            );

            if (archivo.isFile()) {
                extensionFoto = extension;
                break;
            }
        }
    }

    String urlFoto = "";

    if (!extensionFoto.isEmpty()) {
        urlFoto = request.getContextPath()
                + "/imagenesUsuarios/"
                + URLEncoder.encode(nickname, "UTF-8")
                        .replace("+", "%20")
                + extensionFoto;
    }
    
    if (!urlFoto.isEmpty()) {
    urlFoto += "?v=" + System.currentTimeMillis();
}

    // Tipo de usuario
    boolean esEstudiante = usuario instanceof Estudiante;
    boolean esDocente = usuario instanceof Docente;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Perfil de Usuario - edExt</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"rel="stylesheet">
    <link rel="stylesheet"href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">

    <style>
        .perfil-contenedor {
            max-width: 950px;
            margin: 0 auto;
        }

        .perfil-volver {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 48px;
            height: 48px;
            margin-bottom: 24px;
            border: 1px solid #ced4da;
            border-radius: 10px;
            background-color: white;
            color: #212529;
            text-decoration: none;
            transition: background-color 0.2s;
        }

        .perfil-volver:hover {
            background-color: #e9ecef;
            color: #212529;
        }

        .perfil-volver svg {
            width: 28px;
            height: 28px;
        }

        .perfil-cabecera {
            display: flex;
            align-items: center;
            gap: 24px;
            margin-bottom: 24px;
        }

        /* Foto de perfil */
        .perfil-foto-boton {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 120px;
            height: 120px;
            padding: 0;
            border: none;
            border-radius: 8px;
            overflow: hidden;
            background-color: #e9ecef;
            color: #6c757d;
            text-decoration: none;
            flex-shrink: 0;
        }

        .perfil-foto-boton img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .perfil-foto-overlay {
            position: absolute;
            inset: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 6px;
            background-color: rgba(0, 0, 0, 0.55);
            color: white;
            opacity: 0;
            transition: opacity 0.2s;
            font-size: 0.9rem;
            font-weight: 600;
            text-align: center;
        }

        .perfil-foto-boton:hover .perfil-foto-overlay,
        .perfil-foto-boton:focus-visible .perfil-foto-overlay {
            opacity: 1;
        }

        .perfil-foto-boton:focus-visible {
            outline: 3px solid #0d6efd;
            outline-offset: 3px;
        }

        .perfil-nombre {
            font-size: 1.8rem;
            font-weight: 600;
            margin-bottom: 4px;
        }

        .perfil-correo {
            color: #6c757d;
            margin-bottom: 8px;
        }

        .perfil-pestanas {
            display: flex;
            flex-wrap: wrap;
            gap: 4px;
            border-bottom: 2px solid #343a40;
            margin-bottom: 0;
        }

        .perfil-pestana {
            border: 1px solid #adb5bd;
            border-bottom: none;
            border-radius: 8px 8px 0 0;
            padding: 10px 20px;
            background-color: #dee2e6;
            color: #212529;
            font-weight: 600;
            cursor: pointer;
        }

        .perfil-pestana.activa {
            background-color: white;
            position: relative;
            margin-bottom: -2px;
            border-color: #343a40;
        }

        .perfil-panel {
            border: 2px solid #343a40;
            border-radius: 0 0 8px 8px;
            padding: 24px;
            background-color: white;
            min-height: 260px;
            max-height: 520px;
            overflow-y: auto;
        }

        .perfil-dato {
            margin-bottom: 18px;
        }

        .perfil-etiqueta {
            display: block;
            color: #6c757d;
            font-size: 0.9rem;
            margin-bottom: 4px;
        }

        .perfil-valor {
            font-weight: 600;
            overflow-wrap: anywhere;
        }

        .perfil-seccion {
            display: none;
        }

        .perfil-seccion.activa {
            display: block;
        }

        .perfil-elemento {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 16px;
            margin-bottom: 12px;
        }

        .perfil-elemento:last-child {
            margin-bottom: 0;
        }

        .perfil-estado {
            display: inline-block;
            padding: 4px 9px;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 600;
            background-color: #e9ecef;
            color: #495057;
        }

        .perfil-estado.aceptada {
            background-color: #d1e7dd;
            color: #0f5132;
        }

        .perfil-estado.rechazada {
            background-color: #f8d7da;
            color: #842029;
        }

        .perfil-estado.inscripto {
            background-color: #cff4fc;
            color: #055160;
        }

        @media (max-width: 576px) {
            .perfil-cabecera {
                align-items: flex-start;
                gap: 14px;
            }

            .perfil-foto-boton {
                width: 85px;
                height: 85px;
            }

            .perfil-nombre {
                font-size: 1.3rem;
            }

            .perfil-pestana {
                padding: 9px 12px;
                font-size: 0.85rem;
            }

            .perfil-panel {
                padding: 16px;
            }
        }
    </style>
</head>

<body class="bg-light">

    <jsp:include page="header.jsp" />

    <main class="container py-4">

        <% if (usuario == null) { %>

            <div class="alert alert-warning">
                No se encontraron los datos del usuario.
            </div>

            <a href="<%= request.getContextPath() %>/ConsultaUsuarioServlet"
               class="btn btn-secondary">
                Volver a la lista
            </a>

        <% } else { %>

            <div class="perfil-contenedor">

                <% if ("1".equals(request.getParameter("actualizado"))) { %>
                    <div class="alert alert-success" role="alert">
                        Los datos del usuario se actualizaron correctamente.
                    </div>
                <% } %>

                <% if ("1".equals(request.getParameter("fotoActualizada"))) { %>
                    <div class="alert alert-success" role="alert">
                        La foto de perfil se actualizó correctamente.
                    </div>
                <% } %>

                <% if ("1".equals(request.getParameter("errorFoto"))) { %>
                    <div class="alert alert-danger" role="alert">
                        No se pudo actualizar la foto. Verificá el archivo e intentá nuevamente.
                    </div>
                <% } %>

                <!-- Volver a la lista de usuarios -->
                <a href="<%= request.getContextPath() %>/ConsultaUsuarioServlet"
                   class="perfil-volver"
                   title="Volver a la lista de usuarios"
                   aria-label="Volver a la lista de usuarios">

                    <svg xmlns="http://www.w3.org/2000/svg"
                         fill="none"
                         viewBox="0 0 24 24"
                         stroke="currentColor"
                         stroke-width="2"
                         stroke-linecap="round"
                         stroke-linejoin="round">
                        <path d="M19 12H5"/>
                        <path d="M12 19l-7-7 7-7"/>
                    </svg>

                </a>

                <!-- Cabecera del perfil -->
                <div class="perfil-cabecera">

                    <% if (esPropioPerfil) { %>

                        <!-- La foto del propio usuario permite cambiarla -->
                        <a href="<%= request.getContextPath() %>/ModificarImagenPerfilServlet?nickname=<%= URLEncoder.encode(nickname, "UTF-8") %>"
                           class="perfil-foto-boton"
                           title="Cambiar foto de perfil"
                           aria-label="Cambiar foto de perfil">

                            <% if (!urlFoto.isEmpty()) { %>

                                <img src="<%= urlFoto %>"
                                     alt="Foto de perfil de <%= nickname %>">

                            <% } else { %>

                                <span>Sin foto</span>

                            <% } %>

                            <span class="perfil-foto-overlay">
                                Cambiar foto
                            </span>

                        </a>

                    <% } else { %>

                        <!-- En perfiles ajenos la foto no es editable -->
                        <div class="perfil-foto-boton"
                             style="cursor: default">

                            <% if (!urlFoto.isEmpty()) { %>

                                <img src="<%= urlFoto %>"
                                     alt="Foto de perfil de <%= nickname %>">

                            <% } else { %>

                                <span>Sin foto</span>

                            <% } %>

                        </div>

                    <% } %>

                    <div>
                        <h1 class="perfil-nombre">
                            <%= apellido %> <%= nombre %>
                        </h1>

                        <div class="perfil-correo">
                            <%= correo %>
                        </div>

                        <div class="text-secondary small">
                            Nickname: <%= nickname %>
                        </div>

                        <% if (esPropioPerfil) { %>

                            <a href="<%= request.getContextPath() %>/ModificarDatosUsuarioServlet?nickname=<%= URLEncoder.encode(nickname, "UTF-8") %>"
                               class="btn btn-outline-primary mt-2">
                                Modificar datos
                            </a>

                        <% } %>
                        
                        <%
                            Object usuarioSesionAtributo =
                                    session.getAttribute("usuario");

                            boolean haySesion =
                                    usuarioSesionAtributo instanceof Usuario;
                        %>

                        <% if (!esPropioPerfil && haySesion) { %>

                            <form action="<%= request.getContextPath() %>/SeguirUsuarioServlet"
                                  method="post"
                                  class="mt-2">

                                <input type="hidden"
                                       name="nickname"
                                       value="<%= nickname %>">

                                <% if (Boolean.TRUE.equals(request.getAttribute("siguiendo"))) { %>

                                    <input type="hidden"
                                           name="accion"
                                           value="dejarDeSeguir">

                                    <button type="submit"
                                            class="btn btn-outline-secondary">
                                        Dejar de seguir
                                    </button>

                                <% } else { %>

                                    <input type="hidden"
                                           name="accion"
                                           value="seguir">

                                    <button type="submit"
                                            class="btn btn-primary">
                                        Seguir
                                    </button>

                                <% } %>

                            </form>

                        <% } %>
                    </div>

                </div>

                <!-- Pestañas -->
                <div class="perfil-pestanas" role="tablist">

                    <button type="button"
                            class="perfil-pestana activa"
                            data-seccion="general"
                            role="tab"
                            aria-selected="true">
                        GENERAL
                    </button>

                    <button type="button"
                            class="perfil-pestana"
                            data-seccion="cursos"
                            role="tab"
                            aria-selected="false">
                        CURSOS
                    </button>

                    <% if (esEstudiante) { %>
                        <button type="button"
                                class="perfil-pestana"
                                data-seccion="programas"
                                role="tab"
                                aria-selected="false">
                            PROGRAMAS
                        </button>
                    <% } %>

                </div>

                <div class="perfil-panel">

                    <!-- GENERAL -->
                    <section id="general"
                             class="perfil-seccion activa"
                             role="tabpanel">

                        <div class="perfil-dato">
                            <span class="perfil-etiqueta">Nickname</span>
                            <div class="perfil-valor"><%= nickname %></div>
                        </div>

                        <div class="perfil-dato">
                            <span class="perfil-etiqueta">Nombre</span>
                            <div class="perfil-valor"><%= nombre %></div>
                        </div>

                        <div class="perfil-dato">
                            <span class="perfil-etiqueta">Apellido</span>
                            <div class="perfil-valor"><%= apellido %></div>
                        </div>

                        <div class="perfil-dato">
                            <span class="perfil-etiqueta">Correo electrónico</span>
                            <div class="perfil-valor"><%= correo %></div>
                        </div>

                        <div class="perfil-dato">
                            <span class="perfil-etiqueta">Fecha de nacimiento</span>
                            <div class="perfil-valor">
                                <%= fechaNacimiento.isEmpty()
                                        ? "No especificada" : fechaNacimiento %>
                            </div>
                        </div>

                    </section>

                    <!-- CURSOS -->
                    <section id="cursos"
                             class="perfil-seccion"
                             role="tabpanel">

                        <h2 class="h5 mb-3">
                            <%= esEstudiante ? "Mis cursos" : "Ediciones de cursos" %>
                        </h2>

                        <% if (esEstudiante) {
                            Estudiante estudiante = (Estudiante) usuario;
                            boolean hayCursos = false;

                            for (InscripcionCurso inscripcion : estudiante.getCursos()) {
                                EstadoInscripcion estado = inscripcion.getEstado();

                                boolean mostrar =
                                        estado == EstadoInscripcion.INSCRIPTO
                                        || estado == EstadoInscripcion.ACEPTADA
                                        || (esPropioPerfil
                                            && estado == EstadoInscripcion.RECHAZADA);

                                if (!mostrar) {
                                    continue;
                                }

                                EdicionCurso edicion = inscripcion.getCurso();

                                if (edicion == null) {
                                    continue;
                                }

                                hayCursos = true;
                        %>

                            <div class="perfil-elemento">

                                <h3 class="h6 mb-2">
                                    <%= edicion.getNombre() %>
                                </h3>

                                <% if (edicion.getCurso() != null) { %>
                                    <p class="text-secondary mb-2">
                                        Curso: <%= edicion.getCurso().getNombre() %>
                                    </p>
                                <% } %>

                                <span class="perfil-estado <%= estado == EstadoInscripcion.ACEPTADA
                                            ? "aceptada"
                                            : estado == EstadoInscripcion.RECHAZADA
                                                ? "rechazada" : "inscripto" %>">
                                    <%= estado %>
                                </span>

                            </div>

                        <% } %>

                            <% if (!hayCursos) { %>
                                <p class="text-secondary mb-0">
                                    No hay cursos para mostrar.
                                </p>
                            <% } %>

                        <% } else if (esDocente) {
                            Docente docente = (Docente) usuario;
                            boolean hayEdiciones = false;

                            for (EdicionCurso edicion : docente.getEdiciones()) {
                                if (edicion == null) {
                                    continue;
                                }

                                hayEdiciones = true;
                        %>

                            <div class="perfil-elemento">

                                <h3 class="h6 mb-2">
                                    <%= edicion.getNombre() %>
                                </h3>

                                <% if (edicion.getCurso() != null) { %>
                                    <p class="text-secondary mb-0">
                                        Curso: <%= edicion.getCurso().getNombre() %>
                                    </p>
                                <% } %>

                            </div>

                        <% } %>

                            <% if (!hayEdiciones) { %>
                                <p class="text-secondary mb-0">
                                    No hay ediciones de cursos para mostrar.
                                </p>
                            <% } %>

                        <% } else { %>

                            <p class="text-secondary mb-0">
                                No hay cursos asociados a este usuario.
                            </p>

                        <% } %>

                    </section>

                    <!-- PROGRAMAS: solamente para estudiantes -->
                    <% if (esEstudiante) {
                        Estudiante estudiante = (Estudiante) usuario;
                    %>

                        <section id="programas"
                                 class="perfil-seccion"
                                 role="tabpanel">

                            <h2 class="h5 mb-3">Programas de formación</h2>

                            <%
                                boolean hayProgramas = false;

                                for (InscripcionPrograma inscripcion
                                        : estudiante.getProgramas()) {

                                    EstadoInscripcion estado = inscripcion.getEstado();

                                    boolean mostrar =
                                            estado == EstadoInscripcion.INSCRIPTO
                                            || estado == EstadoInscripcion.ACEPTADA
                                            || (esPropioPerfil
                                                && estado == EstadoInscripcion.RECHAZADA);

                                    if (!mostrar) {
                                        continue;
                                    }

                                    ProgramaFormacion programa = inscripcion.getPrograma();

                                    if (programa == null) {
                                        continue;
                                    }

                                    hayProgramas = true;
                            %>

                                <div class="perfil-elemento">

                                    <div class="d-flex flex-wrap
                                                justify-content-between
                                                align-items-start gap-2">

                                        <div>
                                            <h3 class="h6 mb-2">
                                                <a class="link-primary text-decoration-none"
                                                   href="<%= request.getContextPath() %>/ConsultaProgramaServlet?nombre=<%= URLEncoder.encode(programa.getNombre(), "UTF-8") %>">
                                                    <%= programa.getNombre() %>
                                                </a>
                                            </h3>

                                            <p class="text-secondary small mb-2">
                                                Fecha de inscripción:
                                                <%= inscripcion.getFechaIns() != null
                                                        ? inscripcion.getFechaIns().format(
                                                            DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                        : "No especificada" %>
                                            </p>
                                        </div>

                                        <span class="perfil-estado <%= estado == EstadoInscripcion.ACEPTADA
                                                    ? "aceptada"
                                                    : estado == EstadoInscripcion.RECHAZADA
                                                        ? "rechazada" : "inscripto" %>">
                                            <%= estado %>
                                        </span>

                                    </div>

                                </div>

                            <% } %>

                            <% if (!hayProgramas) { %>
                                <p class="text-secondary mb-0">
                                    No hay programas de formación para mostrar.
                                </p>
                            <% } %>

                        </section>

                    <% } %>

                </div>

            </div>

        <% } %>

    </main>

    <script>
        const pestanas = document.querySelectorAll(".perfil-pestana");
        const secciones = document.querySelectorAll(".perfil-seccion");

        pestanas.forEach(function (pestana) {
            pestana.addEventListener("click", function () {
                const destino = this.dataset.seccion;

                pestanas.forEach(function (otraPestana) {
                    otraPestana.classList.remove("activa");
                    otraPestana.setAttribute("aria-selected", "false");
                });

                secciones.forEach(function (seccion) {
                    seccion.classList.remove("activa");
                });

                this.classList.add("activa");
                this.setAttribute("aria-selected", "true");

                const seccionDestino = document.getElementById(destino);

                if (seccionDestino) {
                    seccionDestino.classList.add("activa");
                }
            });
        });
    </script>

</body>
</html>
