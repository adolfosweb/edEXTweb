<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Programa - edExt</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"rel="stylesheet">
    <link rel="stylesheet"href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
    <style>
        .programa-img {
            width: 100%;
            height: 220px;
            object-fit: cover;
        }
        .curso-img {
            width: 100%;
            height: 150px;
            object-fit: cover;
        }
        .cursos-scroll {
        display: flex;
        gap: 1rem;
        overflow-x: auto;
        padding-bottom: 10px;
        }
        .cursos-scroll > * {
            flex: 0 0 calc((100% - 2rem) / 3);
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="d-flex">
        <jsp:include page="menulateral.jsp" />
        <main class="container-fluid py-4 px-4 bg-light">
            <h2 class="mb-4 text-dark">Consultar programa de formación</h2>
            <!-- 1. Seleccionar programa -->
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <label for="programa" class="form-label fw-semibold">Elegir programa</label>
                    <select id="programa" name="programa" class="form-select">
                        <option value="1">Desarrollo Web</option>
                        <option value="2">Programación en Java</option>
                        <option value="3">Introducción a la Informática</option>
                        <option value="4">Desarrollo de Software</option>
                    </select>
                </div>
            </div>
            <!-- 2. Datos del programa -->
            <section class="card shadow-sm mb-4">
                <div class="row g-0">
                    <!-- Imagen ilustrativa -->
                    <div class="col-md-4">
                        <img src="https://placehold.co/600x400/dae8f5/24476b?text=Desarrollo+Web"
                             alt="Imagen del programa Desarrollo Web"
                             class="programa-img rounded-start">
                    </div>
                    <div class="col-md-8">
                        <div class="card-body p-4">
                            <h3 class="card-title mb-3">Desarrollo Web</h3>
                            <p class="text-secondary">
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                            </p>
                            <div class="row">
                                <div class="col-sm-6 mb-2">
                                    <span class="fw-semibold">
                                        <i class="bi bi-calendar-event me-2"></i>
                                        Fecha de inicio
                                    </span>
                                    <p class="mb-0">01/03/2026</p>
                                </div>
                                <div class="col-sm-6 mb-2">
                                    <span class="fw-semibold">
                                        <i class="bi bi-calendar-check me-2"></i>
                                        Fecha de fin
                                    </span>
                                    <p class="mb-0">30/11/2026</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <!-- 3. Cursos del programa -->
            <section class="mb-4">
                <h3 class="h4 mb-3">
                    <i class="bi bi-journal-bookmark me-2"></i>
                    Cursos del programa
                </h3>
                <div class="cursos-scroll g-3">
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <img src="https://placehold.co/600x300/e2e8f0/334155?text=HTML+y+CSS"
                                 class="curso-img card-img-top"
                                 alt="Imagen del curso HTML y CSS">
                            <div class="card-body">
                                <h4 class="h5 card-title">HTML y CSS</h4>
                                <p class="card-text text-secondary">
                                    Fundamentos para crear y diseñar páginas web.
                                </p>
                                <a href="#"class="btn btn-outline-primary btn-sm">Consultar curso<i class="bi bi-arrow-right ms-1"></i></a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <img src="https://placehold.co/600x300/e2e8f0/334155?text=JavaScript"
                                 class="curso-img card-img-top"
                                 alt="Imagen del curso JavaScript">
                            <div class="card-body">
                                <h4 class="h5 card-title">JavaScript</h4>
                                <p class="card-text text-secondary">
                                    Programación e interactividad en aplicaciones web.
                                </p>
                                <a href="#"class="btn btn-outline-primary btn-sm">Consultar curso<i class="bi bi-arrow-right ms-1"></i></a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <img src="https://placehold.co/600x300/e2e8f0/334155?text=Java+Web"
                                 class="curso-img card-img-top"
                                 alt="Imagen del curso Java Web">
                            <div class="card-body">
                                <h4 class="h5 card-title">Java Web</h4>
                                <p class="card-text text-secondary">
                                    Desarrollo de aplicaciones web con Java.
                                </p>
                                <a href="#"class="btn btn-outline-primary btn-sm">Consultar curso<i class="bi bi-arrow-right ms-1"></i></a>
                            </div>
                        </div>
                    </div>   
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <img src="https://placehold.co/600x300/e2e8f0/334155?text=Java+Web"
                                 class="curso-img card-img-top"
                                 alt="Imagen del curso Java Web">
                            <div class="card-body">
                                <h4 class="h5 card-title">Java Web 2</h4>
                                <p class="card-text text-secondary">
                                    Continuacion :D ♥.
                                </p>
                                <a href="#"class="btn btn-outline-primary btn-sm">Consultar curso<i class="bi bi-arrow-right ms-1"></i></a>
                            </div>
                        </div>
                    </div> 
                </div>
            </section>
            <!-- 4. Categorías del programa -->
            <section class="mb-4">
                <h3 class="h4 mb-3">
                    <i class="bi bi-tags me-2"></i>
                    Categorías
                </h3>

                <div class="card shadow-sm">
                    <div class="card-body d-flex flex-wrap gap-2">
                        <span class="badge text-bg-primary p-2">
                            Programación
                        </span>

                        <span class="badge text-bg-primary p-2">
                            Desarrollo Web
                        </span>

                        <span class="badge text-bg-primary p-2">
                            Informática
                        </span>
                    </div>
                </div>
            </section>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>