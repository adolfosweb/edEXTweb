<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Agregar Curso A Programa - edExt</title>
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
                            Agregar curso a programa
                        </h2>
                        <p class="text-secondary mb-0">Seleccione programa de formación y el curso a agregar.</p>
                    </div>
                    <!-- Selección -->
                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <form action="/agregarCursoAPrograma" method="POST">
                                <!-- Programa -->
                                <div class="mb-4">
                                    <label for="programa" class="form-label fw-semibold">Programa de formación</label>
                                    <select id="programa"name="programa"class="form-select"required>
                                        <option value="" selected disabled>Seleccione un programa</option>
                                        <option value="1">Prueba1</option>
                                        <option value="2">Prueba2</option>
                                        <option value="3">Prueba3</option>
                                        <option value="4">Prueba4</option>
                                    </select>
                                </div>
                                <!-- Curso -->
                                <div class="mb-4">
                                    <label for="curso" class="form-label fw-semibold">Curso</label>
                                    <select id="curso"name="curso"class="form-select"required>
                                        <option value="" selected disabled>Seleccione un curso</option>
                                        <option value="1">CPrueba1</option>
                                        <option value="2">CPrueba2</option>
                                        <option value="3">CPrueba3</option>
                                        <option value="4">CPrueba4</option>
                                    </select>
                                </div>
                                <hr class="my-4">
                                <!-- Botones -->
                                <div class="d-flex justify-content-end gap-2">
                                    <button type="button"
                                            class="btn btn-outline-secondary"
                                            onclick="window.history.back();">
                                        <i class="bi bi-x-lg me-1"></i>
                                        Cancelar
                                    </button>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-plus-lg me-1"></i>
                                        Agregar curso
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</body>
</html>
