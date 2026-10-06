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
<body>
    <jsp:include page="header.jsp" />
    <div class="d-flex">
        <jsp:include page="menulateral.jsp" />
        <main class="container py-5 bg-light">
            <div class="row justify-content-center">
                <div class="col-md-8 col-lg-6">                  
                    <h2 class="mb-4 text-dark">Crear programa de formación</h2>                    
                    <form action="/crearPrograma" method="POST"> <!--no se lo del action-->
                        <!-- Nombre -->
                        <div class="mb-3 text-dark">
                            <label for="nombre" class="form-label">Nombre</label>
                            <input type="text"id="nombre"name="nombre"class="form-control"required>
                            <div id="errorNombre"
                                 class="text-danger mt-1">
                            </div>
                        </div>
                        <!-- Descripción -->
                        <div class="mb-3 text-dark">
                            <label for="desc" class="form-label">Descripción</label>
                            <textarea id="desc" name="descripcion" class="form-control" rows="4"></textarea>
                        </div>
                        <!-- Fechas -->
                        <div class="row">
                            <div class="col-md-6 mb-3 text-dark">
                                <label for="fecI" class="form-label">Fecha de inicio</label>
                                <input type="date" id="fecI" name="fechaInicio" class="form-control" required>
                                <div id="errorFecI"
                                 class="text-danger mt-1">
                                </div>
                            </div>
                            <div class="col-md-6 mb-3 text-dark">
                                <label for="fecF" class="form-label">Fecha de fin</label>
                                <input type="date" id="fecF" name="fechaFin" class="form-control" required>
                                <div id="errorFecF"
                                 class="text-danger mt-1">
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">Crear programa</button>
                        <button type="button"class="btn btn-primary" onclick="window.history.go(-1);">Cancelar</button> 
                    </form>
                </div>
            </div>
        </main>
    </div>
</body>
<script src="crearPrograma.js"></script>
</html>
