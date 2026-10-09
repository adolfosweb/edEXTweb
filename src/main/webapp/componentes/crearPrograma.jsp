<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Programa - edExt</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">
    <jsp:include page="header.jsp" />
    <div class="d-flex">
        <jsp:include page="menulateral.jsp" />
        <main class="container-fluid p-4 p-lg-5">
            <div class="row justify-content-center">
                <div class="col-12 col-md-9 col-lg-7 col-xl-6">
                    <!-- Encabezado -->
                    <div class="mb-4">
                        <h2 class="text-dark mb-2">
                            <i class="bi bi-journal-plus me-2 text-primary"></i>
                            Crear programa de formación
                        </h2>
                        <p class="text-secondary mb-0">Complete los datos del nuevo programa de formación.</p>
                    </div>
                    <!-- Formulario -->
                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <form action="/crearPrograma" method="POST">
                                <!-- Nombre -->
                                <div class="mb-4">
                                    <label for="nombre" class="form-label fw-semibold">Nombre del programa</label>
                                    <input type="text"
                                           id="nombre"
                                           name="nombre"
                                           class="form-control"
                                           placeholder="Ingrese el nombre del programa"
                                           required>
                                    <div id="errorNombre" class="text-danger mt-1"></div>
                                </div>
                                <!-- Descripción -->
                                <div class="mb-4">
                                    <label for="desc" class="form-label fw-semibold">Descripción</label>
                                    <textarea id="desc"
                                              name="descripcion"
                                              class="form-control"
                                              rows="4"
                                              placeholder="Describí brevemente el programa..."></textarea>
                                </div>
                                <!-- Fechas -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">Período de vigencia</label>
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="fecI" class="form-label">Fecha de inicio</label>
                                            <input type="date"
                                                   id="fecI"
                                                   name="fechaInicio"
                                                   class="form-control"
                                                   required>
                                            <div id="errorFecI" class="text-danger mt-1"></div>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="fecF" class="form-label">Fecha de fin</label>
                                            <input type="date"
                                                   id="fecF"
                                                   name="fechaFin"
                                                   class="form-control"
                                                   required>
                                            <div id="errorFecF" class="text-danger mt-1"></div>
                                        </div>
                                    </div>
                                </div>
                                <hr class="my-4">
                                <!-- Botones -->
                                <div class="d-flex justify-content-end gap-2">
                                    <button type="button" class="btn btn-outline-secondary" onclick="window.history.back();">
                                        <i class="bi bi-x-lg me-1"></i>
                                        Cancelar
                                    </button>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-check-lg me-1"></i>
                                        Crear programa
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <script src="crearPrograma.js"></script>
</body>
</html>
