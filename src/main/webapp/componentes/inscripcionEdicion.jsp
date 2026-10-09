<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Inscripcion Programa - edExt</title>
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
            <h2 class="mb-4 text-dark">Inscribirse a un curso</h2>
            <!-- 1. Seleccionar programa -->
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <label for="tipo" class="form-label fw-semibold">Cursos de: </label>
                    <select id="tipo" onchange="mostrarLista()" class="form-select">
                        <option value="">Seleccione</option>
                        <option value="instituto">Institutos</option>
                        <option value="categoria">Categorias</option>
                    </select>
                    <hr>

                    <div id="lista-institutos" style="display: none;">
                        <label for="instituto" class="form-label fw-semibold">Institutos </label>
                        <select id="instituto" onchange="" class="form-select">
                            <option value="">Seleccione</option>
                            <option value="1">Instituto 1</option>
                            <option value="2">Instituto 2</option>
                        </select>
                    </div>
                    <div id="lista-categorias" style="display: none;">
                        <label for="categoria" class="form-label fw-semibold">Categorias: </label>
                        <select id="categoria" onchange="" class="form-select">
                            <option value="">Seleccione</option>
                            <option value="1">Categoria 1</option>
                            <option value="2">Categoria 2</option>
                        </select>
                    </div>
                </div>
            </div>
            
            <section class="mb-4">
                <h3 class="h4 mb-3">
                    <i class="bi bi-journal-bookmark me-2"></i>
                    Cursos
                </h3>
                <div class="cursos-scroll g-3">
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <img src="https://placehold.co/600x300/e2e8f0/334155?text=Curso1"
                                 class="curso-img card-img-top"
                                 alt="Imagen del curso HTML y CSS">
                            <div class="card-body">
                                <h4 class="h5 card-title">1</h4>
                                <p class="card-text text-secondary">
                                    Es el primer curso.
                                </p>
                                <a href="#"class="btn btn-outline-primary btn-sm">Inscribirse<i class="bi bi-arrow-right ms-1"></i></a>
                                <a href="consultarPrograma.jsp"class="btn btn-outline-primary btn-sm">Información<i class="bi bi-arrow-right ms-1"></i></a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <img src="https://placehold.co/600x300/e2e8f0/334155?text=Curso2"
                                 class="curso-img card-img-top"
                                 alt="Imagen del curso JavaScript">
                            <div class="card-body">
                                <h4 class="h5 card-title">2</h4>
                                <p class="card-text text-secondary">
                                    Es el segundo curso.
                                </p>
                                <a href="#"class="btn btn-outline-primary btn-sm">Inscribirse<i class="bi bi-arrow-right ms-1"></i></a>
                                <a href="consultarPrograma.jsp"class="btn btn-outline-primary btn-sm">Información<i class="bi bi-arrow-right ms-1"></i></a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <img src="https://placehold.co/600x300/e2e8f0/334155?text=Curso3"
                                 class="curso-img card-img-top"
                                 alt="Imagen del curso Java Web">
                            <div class="card-body">
                                <h4 class="h5 card-title">3</h4>
                                <p class="card-text text-secondary">
                                    Es el tercer curso.
                                </p>
                                <a href="#"class="btn btn-outline-primary btn-sm">Inscribirse<i class="bi bi-arrow-right ms-1"></i></a>
                                <a href="consultarPrograma.jsp"class="btn btn-outline-primary btn-sm">Información<i class="bi bi-arrow-right ms-1"></i></a>
                            </div>
                        </div>
                    </div>   
                    <div class="col-md-6 col-xl-4">
                        <div class="card h-100 shadow-sm">
                            <img src="https://placehold.co/600x300/e2e8f0/334155?text=Curso4"
                                 class="curso-img card-img-top"
                                 alt="Imagen del curso Java Web">
                            <div class="card-body">
                                <h4 class="h5 card-title">4</h4>
                                <p class="card-text text-secondary">
                                    Es el cuarto curso.
                                </p>
                                <a href="#"class="btn btn-outline-primary btn-sm">Inscribirse<i class="bi bi-arrow-right ms-1"></i></a>
                                <a href="consultarPrograma.jsp"class="btn btn-outline-primary btn-sm">Información<i class="bi bi-arrow-right ms-1"></i></a>
                            </div>
                        </div>
                    </div> 
                </div>
            </section>
        </main>
    </div>
    <script>
        function mostrarLista() {
            var seleccion = document.getElementById("tipo").value;
            // Ocultar ambas listas al iniciar el cambio
            document.getElementById("lista-institutos").style.display = "none";
            document.getElementById("lista-categorias").style.display = "none";     
            // Mostrar la lista correspondiente según la selección
            if (seleccion === "instituto") {
                document.getElementById("lista-institutos").style.display = "block";
            } else if (seleccion === "categoria") {
                document.getElementById("lista-categorias").style.display = "block";
            }
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
