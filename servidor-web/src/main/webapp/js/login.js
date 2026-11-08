function mostrarPassword() {

    const password = document.getElementById("password");
    const icono = document.getElementById("iconoPassword");

    if (password.type === "password") {

        password.type = "text";
        icono.className = "bi bi-eye-slash";

    } else {

        password.type = "password";
        icono.className = "bi bi-eye";
    }
}


function validarLogin() {

    const identificador =
            document.getElementById("identificador").value.trim();

    const password =
            document.getElementById("password").value;


    // Limpiar errores anteriores

    document.getElementById("errorIdentificador").textContent = "";
    document.getElementById("errorPassword").textContent = "";


    let valido = true;


    // Usuario

    if (identificador === "") {

        document.getElementById("errorIdentificador").textContent =
                "Ingrese su nickname o correo electrónico.";

        valido = false;
    }


    // Contraseña

    if (password === "") {

        document.getElementById("errorPassword").textContent =
                "Ingrese su contraseña.";

        valido = false;
    }


    return valido;
}