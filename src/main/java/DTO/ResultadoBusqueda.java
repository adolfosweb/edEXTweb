package DTO;

import java.time.LocalDate;

public class ResultadoBusqueda {

    private String tipo;
    private String id;
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private String imagen;

    public ResultadoBusqueda(String tipo, String id, String nombre, String descripcion, LocalDate fecha, String imagen) {
        this.tipo = tipo;
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getImagen() {
        return imagen;
    }
}