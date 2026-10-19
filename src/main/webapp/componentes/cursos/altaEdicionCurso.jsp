<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%
    String nombreCurso = (String) request.getAttribute("curso");
    String nombreEdicion = String.valueOf(request.getAttribute("nombreEdicion") == null
            ? "" : request.getAttribute("nombreEdicion"));
    String fechaInicio = String.valueOf(request.getAttribute("fechaInicio") == null
            ? "" : request.getAttribute("fechaInicio"));
    String fechaFin = String.valueOf(request.getAttribute("fechaFin") == null
            ? "" : request.getAttribute("fechaFin"));
    String cupo = String.valueOf(request.getAttribute("cupo") == null
            ? "" : request.getAttribute("cupo"));
    String edicionOriginal = String.valueOf(request.getAttribute("edicionOriginal") == null
            ? "" : request.getAttribute("edicionOriginal"));
    List<String> docentes = (List<String>) request.getAttribute("docentes");
    Set<String> docentesSeleccionados = (Set<String>) request.getAttribute("docentesSeleccionados");
    boolean modoEdicion = Boolean.TRUE.equals(request.getAttribute("modoEdicion"));
    boolean edicionDuplicada = Boolean.TRUE.equals(request.getAttribute("edicionDuplicada"));
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - <%= modoEdicion ? "Modificar edición" : "Alta de edición" %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .avatar-sm { width: 32px; height: 32px; border-radius: 50%; object-fit: cover; }
    </style>
</head>
<body class="bg-light">
    <jsp:include page="/componentes/header.jsp" />
    <div class="d-flex">
        <jsp:include page="/componentes/menulateral.jsp" />
        <main class="container-fluid p-4 p-lg-5">
            <div class="row justify-content-center">
                <div class="col-12 col-xl-8">
                    <h1 class="h2 mb-2"><%= modoEdicion ? "Modificar edición de curso" : "Alta de edición de curso" %></h1>
                    <p class="text-secondary mb-4">Curso: <strong><%= escapeHtml(nombreCurso) %></strong></p>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger" role="alert"><%= escapeHtml((String) request.getAttribute("error")) %></div>
                    <% } %>
                    <% if (edicionDuplicada) { %>
                    <div class="alert alert-warning">
                        <a href="<%= request.getContextPath() %>/SvltAltaEdicionCurso?modo=editar&amp;curso=<%= java.net.URLEncoder.encode(nombreCurso, "UTF-8") %>&amp;edicion=<%= java.net.URLEncoder.encode((String) request.getAttribute("nombreEdicionDuplicada"), "UTF-8") %>">
                            Modificar los datos de la edición existente
                        </a>
                        <span> o </span>
                        <a href="<%= request.getContextPath() %>/SvltConsultarCurso?nombre=<%= java.net.URLEncoder.encode(nombreCurso, "UTF-8") %>">cancelar</a>.
                    </div>
                    <% } %>

                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <form action="<%= request.getContextPath() %>/SvltAltaEdicionCurso" method="post">
                                <input type="hidden" name="curso" value="<%= escapeHtml(nombreCurso) %>">
                                <% if (modoEdicion) { %>
                                <input type="hidden" name="modo" value="editar">
                                <input type="hidden" name="edicionOriginal" value="<%= escapeHtml(edicionOriginal) %>">
                                <% } %>

                                <div class="mb-3">
                                    <label for="nombre" class="form-label">Nombre de la edición</label>
                                    <input type="text" id="nombre" name="nombre" class="form-control"
                                           value="<%= escapeHtml(nombreEdicion) %>" required
                                           <%= modoEdicion ? "readonly" : "" %>>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="fechaInicio" class="form-label">Fecha de inicio</label>
                                        <input type="date" id="fechaInicio" name="fechaInicio"
                                               class="form-control" value="<%= escapeHtml(fechaInicio) %>"
                                               <%= modoEdicion ? "" : "min=\"" + java.time.LocalDate.now() + "\"" %> required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="fechaFin" class="form-label">Fecha de fin</label>
                                        <input type="date" id="fechaFin" name="fechaFin"
                                               class="form-control" value="<%= escapeHtml(fechaFin) %>"
                                               <%= modoEdicion ? "" : "min=\"" + fechaFinMinima(fechaInicio) + "\"" %> required>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="cupo" class="form-label">Cupo (opcional)</label>
                                    <input type="number" id="cupo" name="cupo" min="0"
                                           class="form-control" value="<%= escapeHtml(cupo) %>">
                                    <div class="form-text">Déjalo en blanco si no se define un cupo.</div>
                                </div>

                                <div class="mb-4">
                                    <label for="docentes" class="form-label">Docentes participantes</label>
                                    <select id="docentes" name="docentes" class="form-select" multiple size="6" required>
                                        <% if (docentes != null) {
                                            for (String nickname : docentes) { %>
                                        <option value="<%= escapeHtml(nickname) %>"
                                            <%= docentesSeleccionados != null && docentesSeleccionados.contains(nickname)
                                                    ? "selected" : "" %>>
                                            <%= escapeHtml(nickname) %>
                                        </option>
                                        <%  }
                                        } %>
                                    </select>
                                    <div class="form-text">Para seleccionar varios, mantené apretado CTRL.</div>
                                </div>

                                <div class="d-flex justify-content-end gap-2">
                                    <a href="<%= request.getContextPath() %>/SvltConsultarCurso?nombre=<%= java.net.URLEncoder.encode(nombreCurso, "UTF-8") %>"
                                       class="btn btn-outline-secondary">Cancelar</a>
                                    <button type="submit" class="btn btn-primary">
                                        <%= modoEdicion ? "Guardar cambios" : "Dar de alta edición" %>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    <% if (!modoEdicion) { %>
    <script>
        const fechaInicioInput = document.getElementById("fechaInicio");
        const fechaFinInput = document.getElementById("fechaFin");
        fechaInicioInput.addEventListener("change", () => {
            if (fechaInicioInput.value) {
                const siguienteDia = new Date(fechaInicioInput.value + "T00:00:00");
                siguienteDia.setDate(siguienteDia.getDate() + 1);
                fechaFinInput.min = [
                    siguienteDia.getFullYear(),
                    String(siguienteDia.getMonth() + 1).padStart(2, "0"),
                    String(siguienteDia.getDate()).padStart(2, "0")
                ].join("-");
            }
            if (fechaFinInput.value && fechaFinInput.value <= fechaInicioInput.value) {
                fechaFinInput.value = "";
            }
        });
    </script>
    <% } %>
</body>
</html>
<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String fechaFinMinima(String fechaInicio) {
        try {
            return java.time.LocalDate.parse(fechaInicio).plusDays(1).toString();
        } catch (java.time.format.DateTimeParseException ex) {
            return java.time.LocalDate.now().plusDays(1).toString();
        }
    }
%>
