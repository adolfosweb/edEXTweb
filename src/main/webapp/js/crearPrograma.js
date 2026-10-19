function validarPrograma() {

    const nombre = document.getElementById("nombre").value.trim();
    const fecInicio = document.getElementById("fecI").value;
    const fecFin = document.getElementById("fecF").value;

    // Limpiar errores anteriores
    document.getElementById("errorNombre").textContent = "";
    document.getElementById("errorFecI").textContent = "";
    document.getElementById("errorFecF").textContent = "";

    let valido = true;
    
    // Nombre
    if (nombre === "") {
        document.getElementById("errorNombre").textContent = "Ingrese un nombre.";
        valido = false;
    }
    // Fecha de inicio
    if (fecInicio === "") {
        document.getElementById("errorFecI").textContent = "Ingrese una fecha de inicio.";
        valido = false;
    }
    // Fecha de fin
    if (fecFin === "") {
        document.getElementById("errorFecF").textContent = "Ingrese una fecha de fin.";
        valido = false;
    }
    // Comprobar el período de vigencia
    if (fecInicio !== "" && fecFin !== "" && fecFin <= fecInicio) {
        document.getElementById("errorFecF").textContent = "La fecha de fin debe ser posterior a la de inicio.";
        valido = false;
    }
    return valido;
}

