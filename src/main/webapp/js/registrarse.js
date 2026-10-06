function mostrarPassword(campoId, iconoId) {

    const password = document.getElementById(campoId);
    const icono = document.getElementById(iconoId);

    if (password.type === "password") {

        password.type = "text";
        icono.className = "bi bi-eye-slash";

    } else {

        password.type = "password";
        icono.className = "bi bi-eye";

    }
}


function mostrarInstituto() {

    const docente = document.getElementById("docente");
    const contenedor = document.getElementById("contenedorInstituto");

    if (docente.checked) {

        contenedor.style.display = "block";

    } else {

        contenedor.style.display = "none";

        document.getElementById("errorInstituto").textContent = "";
    }
}


function validarRegistro() {

    const nickname = document.getElementById("nickname").value.trim();
    const nombre = document.getElementById("nombre").value.trim();
    const apellido = document.getElementById("apellido").value.trim();
    const email = document.getElementById("email").value.trim();

    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    const fechaNacimiento =
            document.getElementById("fechaNacimiento").value;

    const docente =
            document.getElementById("docente").checked;

    const instituto =
            document.getElementById("instituto").value;


    // Limpiar errores anteriores

    document.getElementById("errorNickname").textContent = "";
    document.getElementById("errorNombre").textContent = "";
    document.getElementById("errorApellido").textContent = "";
    document.getElementById("errorEmail").textContent = "";
    document.getElementById("errorPassword").textContent = "";
    document.getElementById("errorConfirmPassword").textContent = "";
    document.getElementById("errorFechaNacimiento").textContent = "";
    document.getElementById("errorInstituto").textContent = "";


    let valido = true;


    // Nickname

    if (nickname === "") {

        document.getElementById("errorNickname").textContent =
                "Ingrese un nickname.";

        valido = false;
    }


    // Nombre

    if (nombre === "") {

        document.getElementById("errorNombre").textContent =
                "Ingrese su nombre.";

        valido = false;
    }


    // Apellido

    if (apellido === "") {

        document.getElementById("errorApellido").textContent =
                "Ingrese su apellido.";

        valido = false;
    }


    // Email

    if (email === "") {

        document.getElementById("errorEmail").textContent =
                "Ingrese su correo electrónico.";

        valido = false;
    }


    // Contraseña

    if (password === "") {

        document.getElementById("errorPassword").textContent =
                "Ingrese una contraseña.";

        valido = false;
    }


    // Confirmar contraseña

    if (confirmPassword === "") {

        document.getElementById("errorConfirmPassword").textContent =
                "Confirme su contraseña.";

        valido = false;

    } else if (password !== confirmPassword) {

        document.getElementById("errorConfirmPassword").textContent =
                "Las contraseñas no coinciden.";

        valido = false;
    }


    // Fecha de nacimiento

    if (fechaNacimiento === "") {

        document.getElementById("errorFechaNacimiento").textContent =
                "Ingrese su fecha de nacimiento.";

        valido = false;

    } else {

        const fecha = new Date(fechaNacimiento);
        const hoy = new Date();

        if (fecha > hoy) {

            document.getElementById("errorFechaNacimiento").textContent =
                    "La fecha no puede ser posterior a hoy.";

            valido = false;
        }
    }


    // Instituto

    if (docente && instituto === "") {

        document.getElementById("errorInstituto").textContent =
                "Seleccione un instituto.";

        valido = false;
    }


    return valido;
}