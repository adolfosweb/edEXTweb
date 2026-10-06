<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Iniciar sesión - edExt</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
</head>

<body>

<div class="container mt-5">

    <div class="row justify-content-center">

        <div class="col-md-6 col-lg-5">

            <div class="card shadow">

                <div class="card-body p-4 bg-dark">

                    <h2 class="text-center mb-4">
                        Iniciar sesión
                    </h2>


                    <%
                        String error = (String) request.getAttribute("error");

                        if (error != null) {
                    %>

                        <div class="text-danger mb-3">
                            <%= error %>
                        </div>

                    <%
                        }
                    %>


                    <form action="${pageContext.request.contextPath}/LoginServlet"
                          method="post"
                          onsubmit="return validarLogin()">


                        <!-- Usuario -->
                        <div class="mb-3">

                            <label for="identificador" class="form-label">
                                Nickname o correo electrónico
                            </label>

                            <input type="text"
                                   class="form-control"
                                   id="identificador"
                                   name="identificador">

                            <div id="errorIdentificador"
                                 class="text-danger mt-1">
                            </div>

                        </div>


                        <!-- Contraseña -->
                        <div class="mb-3">

                            <label for="password" class="form-label">
                                Contraseña
                            </label>

                            <div class="input-group">

                                <input type="password"
                                       class="form-control"
                                       id="password"
                                       name="password">

                                <button type="button"
                                        class="btn btn-outline-secondary"
                                        onclick="mostrarPassword()">

                                    <i class="bi bi-eye"
                                       id="iconoPassword">
                                    </i>

                                </button>

                            </div>

                            <div id="errorPassword"
                                 class="text-danger mt-1">
                            </div>

                        </div>


                        <!-- Botones -->
                        <div class="d-flex justify-content-between mt-4">

                            <a href="${pageContext.request.contextPath}/componentes/principal.jsp"
                               class="btn btn-secondary">
                                Cancelar
                            </a>

                            <button type="submit"
                                    class="btn btn-primary">
                                Ingresar
                            </button>

                        </div>

                    </form>

                </div>

            </div>

        </div>

    </div>

</div>


<script src="${pageContext.request.contextPath}/js/login.js"></script>

</body>
</html>