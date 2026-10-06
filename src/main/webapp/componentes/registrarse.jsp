<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Registrarse - edExt</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
</head>

<body>

<div class="container mt-5 mb-5">

    <div class="row justify-content-center">

        <div class="col-md-6 col-lg-5">

            <div class="card shadow">

                <div class="card-body p-4">

                    <h2 class="text-center mb-4">
                        Crear cuenta
                    </h2>

                    <form action="${pageContext.request.contextPath}/RegistroServlet"
                          method="post"
                          onsubmit="return validarRegistro()">

                        <!-- Nickname -->
                        <div class="mb-3">

                            <label for="nickname" class="form-label">
                                Nickname
                            </label>

                            <input type="text"
                                   class="form-control"
                                   id="nickname"
                                   name="nickname">

                            <div id="errorNickname"
                                 class="text-danger mt-1">
                            </div>

                        </div>


                        <!-- Nombre -->
                        <div class="mb-3">

                            <label for="nombre" class="form-label">
                                Nombre
                            </label>

                            <input type="text"
                                   class="form-control"
                                   id="nombre"
                                   name="nombre">

                            <div id="errorNombre"
                                 class="text-danger mt-1">
                            </div>

                        </div>


                        <!-- Apellido -->
                        <div class="mb-3">

                            <label for="apellido" class="form-label">
                                Apellido
                            </label>

                            <input type="text"
                                   class="form-control"
                                   id="apellido"
                                   name="apellido">

                            <div id="errorApellido"
                                 class="text-danger mt-1">
                            </div>

                        </div>


                        <!-- Email -->
                        <div class="mb-3">

                            <label for="email" class="form-label">
                                Correo electrónico
                            </label>

                            <input type="email"
                                   class="form-control"
                                   id="email"
                                   name="email">

                            <div id="errorEmail"
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
                                        onclick="mostrarPassword('password', 'iconoPassword')">

                                    <i class="bi bi-eye"
                                       id="iconoPassword">
                                    </i>

                                </button>

                            </div>

                            <div id="errorPassword"
                                 class="text-danger mt-1">
                            </div>

                        </div>


                        <!-- Confirmar contraseña -->
                        <div class="mb-3">

                            <label for="confirmPassword" class="form-label">
                                Confirmar contraseña
                            </label>

                            <div class="input-group">

                                <input type="password"
                                       class="form-control"
                                       id="confirmPassword"
                                       name="confirmPassword">

                                <button type="button"
                                        class="btn btn-outline-secondary"
                                        onclick="mostrarPassword('confirmPassword', 'iconoConfirmPassword')">

                                    <i class="bi bi-eye"
                                       id="iconoConfirmPassword">
                                    </i>

                                </button>

                            </div>

                            <div id="errorConfirmPassword"
                                 class="text-danger mt-1">
                            </div>

                        </div>


                        <!-- Fecha de nacimiento -->
                        <div class="mb-3">

                            <label for="fechaNacimiento" class="form-label">
                                Fecha de nacimiento
                            </label>

                            <input type="date"
                                   class="form-control"
                                   id="fechaNacimiento"
                                   name="fechaNacimiento">

                            <div id="errorFechaNacimiento"
                                 class="text-danger mt-1">
                            </div>

                        </div>


                        <!-- Docente -->
                        <div class="form-check mb-3">

                            <input class="form-check-input"
                                   type="checkbox"
                                   id="docente"
                                   name="docente"
                                   onchange="mostrarInstituto()">

                            <label class="form-check-label"
                                   for="docente">
                                Soy docente
                            </label>

                        </div>


                        <!-- Instituto -->
                        <div class="mb-3"
                        id="contenedorInstituto"
                        style="display: none;">

                       <label for="instituto" class="form-label">
                           Instituto
                       </label>

                       <select class="form-select"
                               id="instituto"
                               name="instituto">

                           <option value="">
                               Seleccione un instituto
                           </option>

                           <%
                               java.util.Collection<Logica.Instituto> institutos =
                                       (java.util.Collection<Logica.Instituto>)
                                       request.getAttribute("institutos");

                               if (institutos != null) {

                                   for (Logica.Instituto instituto : institutos) {

                                       String seleccionado =
                                               instituto.getNombre().equals(
                                                       request.getParameter("instituto")
                                               )
                                               ? "selected"
                                               : "";
                           %>

                               <option value="<%= instituto.getNombre() %>"
                                       <%= seleccionado %>>
                                   <%= instituto.getNombre() %>
                               </option>

                           <%
                                   }
                               }
                           %>

                       </select>

                       <div id="errorInstituto"
                            class="text-danger mt-1">
                           <%
                               String errorInstituto =
                                       (String) request.getAttribute("errorInstituto");

                               if (errorInstituto != null) {
                                   out.print(errorInstituto);
                               }
                           %>
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
                                Registrarse
                            </button>

                        </div>

                    </form>

                </div>

            </div>

        </div>

    </div>

</div>


<script src="${pageContext.request.contextPath}/js/registrarse.js"></script>

</body>
</html>