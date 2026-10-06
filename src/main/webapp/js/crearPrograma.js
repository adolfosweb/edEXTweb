function validarPrograma(){
    const nombre = document.getElementById("nombre").value.trim;
    //const desc = document.getElementById("desc").value.trim;
    const fecInicio = document.getElementById("fecI").value;
    const fecFin = document.getElementById("fecF").value;
    
    document.getElementById("nombre").textContent = "";
    document.getElementById("fecI").textContent = "";
    document.getElementById("fecF").textContent = "";
    
    let valido = true;
    
    if(nombre === ""){
        document.getElementById("errorNombre").textContent = "Ingrese un nombre";
        valido = false;
    }
    if(fecInicio === ""){
        document.getElementById("errorFecI").textContent = "Ingrese una fecha";
        valido = false;
    }
    if(fecFin === ""){
        document.getElementById("errorFecF").textContent = "Ingrese una fecha";
        valido = false;
    }
    return valido;
}
