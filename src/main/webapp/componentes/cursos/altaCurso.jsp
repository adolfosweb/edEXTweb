<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Datatypes.DtCurso"%>
<%@page import="java.util.List"%>
<%
    DtCurso curso = (DtCurso) request.getAttribute("cursoFormulario");
    List<String> institutos = (List<String>) request.getAttribute("institutos");
    List<String> cursosExistentes = (List<String>) request.getAttribute("cursosExistentes");
    Object previasSeleccionadas = request.getAttribute("previasSeleccionadas");
    boolean modoEdicion = Boolean.TRUE.equals(request.getAttribute("modoEdicion"));
    boolean mostrarExito = Boolean.TRUE.equals(request.getAttribute("mostrarExito"));
    if (curso == null) {
        curso = new DtCurso();
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt - <%= modoEdicion ? "Modificar curso" : "Alta de curso" %></title>
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
                <div class="col-12 col-xl-9">
                    <h1 class="h2 mb-4"><%= modoEdicion ? "Modificar curso" : "Alta de curso" %></h1>

                    <% if (mostrarExito) { %>
                    <div class="alert alert-success" role="status">El curso fue dado de alta correctamente.</div>
                    <% } %>
                    <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger" role="alert"><%= escapeHtml((String) request.getAttribute("error")) %></div>
                    <% } %>
                    <% if (Boolean.TRUE.equals(request.getAttribute("cursoDuplicado"))) { %>
                    <div class="alert alert-warning">
                        <a href="<%= request.getContextPath() %>/AltaCurso?modo=editar&amp;nombre=<%= java.net.URLEncoder.encode(curso.getNombre(), "UTF-8") %>">
                            Modificar los datos del curso existente
                        </a>
                        <span> o </span>
                        <a href="<%= request.getContextPath() %>/SvltListarCursos">cancelar el alta</a>.
                    </div>
                    <% } %>

                    <div class="alert alert-info" role="note">
                        Las categorías todavía no están disponibles: el modelo actual no permite asociarlas ni guardarlas.
                    </div>

                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <form action="<%= request.getContextPath() %>/AltaCurso" method="post">
                                <% if (modoEdicion) { %>
                                <input type="hidden" name="modo" value="editar">
                                <% } %>

                                <div class="mb-3">
                                    <label for="nombre" class="form-label">Nombre del curso</label>
                                    <input type="text" id="nombre" name="nombre" class="form-control"
                                           value="<%= escapeHtml(curso.getNombre()) %>" required
                                           <%= modoEdicion ? "readonly" : "" %>>
                                </div>

                                <div class="mb-3">
                                    <label for="descripcion" class="form-label">Descripción</label>
                                    <textarea id="descripcion" name="descripcion" class="form-control"
                                              rows="4" required><%= escapeHtml(curso.getDescripcion()) %></textarea>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="duracion" class="form-label">Duración</label>
                                        <input type="text" id="duracion" name="duracion" class="form-control"
                                               value="<%= escapeHtml(curso.getDuracion()) %>" required>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <label for="cantHoras" class="form-label">Cantidad de horas</label>
                                        <input type="number" id="cantHoras" name="cantHoras" min="0"
                                               class="form-control" value="<%= curso.getCantHoras() %>" required>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <label for="cantCreditos" class="form-label">Créditos</label>
                                        <input type="number" id="cantCreditos" name="cantCreditos" min="0"
                                               class="form-control" value="<%= curso.getCantCreditos() %>" required>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="url" class="form-label">URL asociada</label>
                                    <input type="url" id="url" name="url" class="form-control"
                                           value="<%= escapeHtml(curso.getUrl()) %>" required>
                                </div>

                                <div class="mb-3">
                                    <label for="instituto" class="form-label">Instituto que brindará el curso</label>
                                    <select id="instituto" name="instituto" class="form-select" required>
                                        <option value="">Selecciona un instituto</option>
                                        <% if (institutos != null) {
                                            for (String instituto : institutos) { %>
                                        <option value="<%= escapeHtml(instituto) %>"
                                            <%= instituto.equals(curso.getInstituto()) ? "selected" : "" %>>
                                            <%= escapeHtml(instituto) %>
                                        </option>
                                        <%  }
                                        } %>
                                    </select>
                                    <% if (institutos == null || institutos.isEmpty()) { %>
                                    <div class="form-text text-danger">Tu cuenta docente no tiene institutos asociados.</div>
                                    <% } %>
                                </div>

                                <div class="mb-4">
                                    <label for="previas" class="form-label">Cursos previos</label>
                                    <select id="previas" name="previas" class="form-select" multiple size="5">
                                        <% if (cursosExistentes != null) {
                                            for (String nombreCurso : cursosExistentes) { %>
                                        <option value="<%= escapeHtml(nombreCurso) %>"
                                            <%= estaSeleccionado(previasSeleccionadas, nombreCurso) ? "selected" : "" %>>
                                            <%= escapeHtml(nombreCurso) %>
                                        </option>
                                        <%  }
                                        } %>
                                    </select>
                                    <div class="form-text">Opcional. Puedes seleccionar ninguno, uno o varios cursos.</div>
                                </div>

                                <div class="d-flex justify-content-end gap-2">
                                    <a href="<%= request.getContextPath() %>/SvltListarCursos"
                                       class="btn btn-outline-secondary">Cancelar</a>
                                    <button type="submit" class="btn btn-primary">
                                        <%= modoEdicion ? "Guardar cambios" : "Dar de alta curso" %>
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

    private boolean estaSeleccionado(Object seleccion, String valor) {
        if (seleccion instanceof List<?>) {
            return ((List<?>) seleccion).contains(valor);
        }
        if (seleccion instanceof String[]) {
            for (String item : (String[]) seleccion) {
                if (valor.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }
%>
