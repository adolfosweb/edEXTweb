<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="com.mycompany.edextweb.soap.UsuarioDTO"%>
<%@page import="com.mycompany.edextweb.soap.PerfilUsuarioDTO"%>
<%@page import="com.mycompany.edextweb.soap.InscripcionCursoDTO"%>
<%@page import="com.mycompany.edextweb.soap.InscripcionProgramaDTO"%>
<%@page import="com.mycompany.edextweb.soap.EdicionPerfilDTO"%>

<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.io.File"%>
<%@page import="java.util.List"%>

<%
    // Perfil recibido desde PerfilUsuarioServlet
    PerfilUsuarioDTO perfil = (PerfilUsuarioDTO) request.getAttribute("perfil");

    UsuarioDTO usuario = (UsuarioDTO) request.getAttribute("usuario");

    boolean esPropioPerfil = Boolean.TRUE.equals(request.getAttribute("esPropioPerfil"));

    String nickname = usuario != null && usuario.getNickname() != null ? usuario.getNickname() : "";

    String nombre = usuario != null && usuario.getNombre() != null ? usuario.getNombre() : "";

    String apellido = usuario != null && usuario.getApellido() != null ? usuario.getApellido() : "";

    String correo = usuario != null && usuario.getCorreoElectronico() != null ? usuario.getCorreoElectronico() : "";

    String fechaNacimiento = "";

    if (usuario != null && usuario.getFecNac() != null && !usuario.getFecNac().isEmpty()) {

        try {
            fechaNacimiento = LocalDate.parse(usuario.getFecNac()).format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            fechaNacimiento = usuario.getFecNac();
        }
    }

    // Tipo de usuario
    boolean esEstudiante = usuario != null && "ESTUDIANTE".equalsIgnoreCase(usuario.getTipoUsuario());

    boolean esDocente = usuario != null && "DOCENTE".equalsIgnoreCase(usuario.getTipoUsuario());

    String urlFoto = "";
String rutaImagen = usuario != null ? usuario.getImagen() : null;

if (rutaImagen != null && !rutaImagen.isBlank()) {
    urlFoto = request.getContextPath()
            + "/imagen?ruta=" + URLEncoder.encode(rutaImagen, "UTF-8");
    // Solo fuerza recarga justo después de subir una foto
    if ("1".equals(request.getParameter("fotoActualizada"))) {
        urlFoto += "&v=" + System.currentTimeMillis();
    }
}

    // Colecciones recibidas mediante SOAP
    List<InscripcionCursoDTO> inscripcionesCursos = perfil != null ? perfil.getInscripcionesCursos() : null;

    List<InscripcionProgramaDTO> inscripcionesProgramas = perfil != null ? perfil.getInscripcionesProgramas() : null;

    List<EdicionPerfilDTO> edicionesDocente = perfil != null ? perfil.getEdicionesDocente() : null;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Perfil de Usuario - edExt</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">

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

    <div class="d-flex">
        <jsp:include page="menulateral.jsp"/>

        <main class="container-fluid py-4 px-4">

            <% if (usuario == null || perfil == null) { %>

                <div class="alert alert-warning">
                    No se encontraron los datos del usuario.
                </div>

                <a href="<%= request.getContextPath() %>/buscador"
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

                    <!-- Volver -->
                    <a href="javascript:history.back()"
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

                    <!-- Cabecera -->
                    <div class="perfil-cabecera">

                        <% if (esPropioPerfil) { %>

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

                            <div class="perfil-foto-boton" style="cursor: default">

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
                                        usuarioSesionAtributo instanceof UsuarioDTO;
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

                                boolean hayCursos = false;

                                if (inscripcionesCursos != null) {
                                    for (InscripcionCursoDTO inscripcion : inscripcionesCursos) {

                                        String estado = inscripcion.getEstado();

                                        boolean mostrar =
                                                "INSCRIPTO".equals(estado)
                                                || "ACEPTADA".equals(estado)
                                                || (esPropioPerfil && "RECHAZADA".equals(estado));

                                        if (!mostrar) {
                                            continue;
                                        }

                                        String nombreEdicion = inscripcion.getNombreEdicion();
                                        String nombreCurso = inscripcion.getNombreCurso();

                                        if (nombreEdicion == null) {
                                            continue;
                                        }

                                        hayCursos = true;
                            %>

                                <div class="perfil-elemento">

                                    <div class="d-flex flex-wrap justify-content-between align-items-start gap-2">

                                        <div>
                                            <h3 class="h6 mb-2">
                                                <%= nombreEdicion %>
                                            </h3>

                                            <% if (nombreCurso != null) { %>
                                                <p class="text-secondary mb-2">
                                                    Curso: <%= nombreCurso %>
                                                </p>
                                            <% } %>
                                        </div>

                                        <div>
                                            <span class="perfil-estado <%= "ACEPTADA".equals(estado)
                                                    ? "aceptada"
                                                    : "RECHAZADA".equals(estado)
                                                        ? "rechazada" : "inscripto" %>">
                                                <%= estado %>
                                            </span>

                                            <% if (nombreCurso != null) { %>
                                                <div class="mt-3">
                                                    <a href="<%= request.getContextPath() %>/SvltConsultarCurso?nombre=<%= URLEncoder.encode(nombreCurso, "UTF-8") %>&amp;edicion=<%= URLEncoder.encode(nombreEdicion, "UTF-8") %>"
                                                       class="btn btn-outline-primary btn-sm">
                                                        Consultar edición
                                                        <i class="bi bi-arrow-right ms-1"></i>
                                                    </a>
                                                </div>
                                            <% } %>
                                        </div>

                                    </div>
                                </div>

                            <%      }
                                }
                            %>

                                <% if (!hayCursos) { %>
                                    <p class="text-secondary mb-0">
                                        No hay cursos para mostrar.
                                    </p>
                                <% } %>

                            <% } else if (esDocente) {

                                boolean hayEdiciones = false;

                                if (edicionesDocente != null) {
                                    for (EdicionPerfilDTO edicion : edicionesDocente) {

                                        if (edicion == null || edicion.getNombreEdicion() == null) {
                                            continue;
                                        }

                                        hayEdiciones = true;

                                        String nombreEdicion = edicion.getNombreEdicion();
                                        String nombreCurso = edicion.getNombreCurso();

                                        String idAceptados = "aceptados_"
                                                + Integer.toUnsignedString(nombreEdicion.hashCode());

                                        List<UsuarioDTO> aceptados =
                                                edicion.getEstudiantesAceptados();
                            %>

                                <div class="perfil-elemento">

                                    <div class="d-flex flex-wrap justify-content-between align-items-start gap-2">

                                        <div>
                                            <h3 class="h6 mb-2">
                                                <%= nombreEdicion %>
                                            </h3>

                                            <% if (nombreCurso != null) { %>
                                                <p class="text-secondary mb-0">
                                                    Curso: <%= nombreCurso %>
                                                </p>
                                            <% } %>
                                        </div>

                                        <div class="mt-3 d-flex flex-wrap gap-2">

                                            <% if (nombreCurso != null) { %>
                                                <a href="<%= request.getContextPath() %>/SvltConsultarCurso?nombre=<%= URLEncoder.encode(nombreCurso, "UTF-8") %>&amp;edicion=<%= URLEncoder.encode(nombreEdicion, "UTF-8") %>"
                                                   class="btn btn-outline-primary btn-sm">
                                                    Consultar edición
                                                    <i class="bi bi-arrow-right ms-1"></i>
                                                </a>
                                            <% } %>

                                            <% if (esPropioPerfil) { %>
                                                <button type="button"
                                                        class="btn btn-outline-success btn-sm"
                                                        data-bs-toggle="collapse"
                                                        data-bs-target="#<%= idAceptados %>"
                                                        aria-expanded="false"
                                                        aria-controls="<%= idAceptados %>">
                                                    Ver estudiantes aceptados
                                                </button>
                                            <% } %>

                                        </div>
                                    </div>

                                    <% if (esPropioPerfil) { %>

                                        <div class="collapse mt-3" id="<%= idAceptados %>">

                                            <% if (aceptados == null || aceptados.isEmpty()) { %>

                                                <p class="text-secondary small mb-0">
                                                    No hay estudiantes aceptados en esta edición.
                                                </p>

                                            <% } else { %>

                                                <ul class="list-group">

                                                    <% for (UsuarioDTO estudiante : aceptados) { %>

                                                        <li class="list-group-item">

                                                            <strong>
                                                                <%= estudiante.getNombre() %>
                                                                <%= estudiante.getApellido() %>
                                                            </strong>

                                                            <div class="text-secondary small">
                                                                Nickname: <%= estudiante.getNickname() %>
                                                            </div>

                                                        </li>

                                                    <% } %>

                                                </ul>

                                            <% } %>

                                        </div>

                                    <% } %>

                                </div>

                            <%      }
                                }
                            %>

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

                        <!-- PROGRAMAS -->
                        <% if (esEstudiante) { %>

                            <section id="programas"
                                     class="perfil-seccion"
                                     role="tabpanel">

                                <h2 class="h5 mb-3">Programas de formación</h2>
                                
                                <% if (esPropioPerfil) { %>
                                    <form action="<%= request.getContextPath() %>/SvltGenerarActa" method="get"
                                          class="row g-2 align-items-end mb-4">
                                        <div class="col-12 col-md-8">
                                            <label for="programaActa" class="form-label">Generar acta de estudios</label>
                                            <select id="programaActa" name="programa" class="form-select" required>
                                                <option value="" selected disabled>Elegí un programa de formación</option>
                                                <% if (inscripcionesProgramas != null) {
                                                       for (InscripcionProgramaDTO ip : inscripcionesProgramas) {
                                                           if ("ACEPTADA".equals(ip.getEstado()) && ip.getNombrePrograma() != null) { %>
                                                    <option value="<%= ip.getNombrePrograma() %>"><%= ip.getNombrePrograma() %></option>
                                                <%         }
                                                       }
                                                   } %>
                                            </select>
                                        </div>
                                        <div class="col-12 col-md-4">
                                            <button type="submit" class="btn btn-success w-100">
                                                <i class="bi bi-file-earmark-pdf me-1"></i>Generar acta
                                            </button>
                                        </div>
                                    </form>
                                <% } %>
                                
                                <%
                                    boolean hayProgramas = false;

                                    if (inscripcionesProgramas != null) {
                                        for (InscripcionProgramaDTO inscripcion : inscripcionesProgramas) {

                                            String estado = inscripcion.getEstado();

                                            boolean mostrar =
                                                    "INSCRIPTO".equals(estado)
                                                    || "ACEPTADA".equals(estado)
                                                    || (esPropioPerfil && "RECHAZADA".equals(estado));

                                            if (!mostrar) {
                                                continue;
                                            }

                                            String nombrePrograma = inscripcion.getNombrePrograma();

                                            if (nombrePrograma == null) {
                                                continue;
                                            }

                                            hayProgramas = true;

                                            String fechaInscripcion = "";

                                            if (inscripcion.getFechaInscripcion() != null
                                                    && !inscripcion.getFechaInscripcion().isEmpty()) {
                                                try {
                                                    fechaInscripcion = LocalDate.parse(
                                                            inscripcion.getFechaInscripcion()
                                                    ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                                } catch (Exception e) {
                                                    fechaInscripcion = inscripcion.getFechaInscripcion();
                                                }
                                            }
                                %>

                                    <div class="perfil-elemento">

                                        <div class="d-flex flex-wrap justify-content-between align-items-start gap-2">

                                            <div>
                                                <h3 class="h6 mb-2">
                                                    <%= nombrePrograma %>
                                                </h3>

                                                <p class="text-secondary small mb-2">
                                                    Fecha de inscripción:
                                                    <%= fechaInscripcion.isEmpty()
                                                            ? "No especificada" : fechaInscripcion %>
                                                </p>
                                            </div>

                                            <div class="d-flex flex-column align-items-start gap-2">

                                                <span class="perfil-estado <%= "ACEPTADA".equals(estado)
                                                        ? "aceptada"
                                                        : "RECHAZADA".equals(estado)
                                                            ? "rechazada" : "inscripto" %>">
                                                    <%= estado %>
                                                </span>

                                                <a href="<%= request.getContextPath() %>/consultarPrograma?programa=<%= URLEncoder.encode(nombrePrograma, "UTF-8") %>"
                                                   class="btn btn-outline-primary btn-sm">
                                                    Consultar programa
                                                    <i class="bi bi-arrow-right ms-1"></i>
                                                </a>

                                            </div>

                                        </div>

                                    </div>

                                <%      }
                                    }
                                %>

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
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

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
