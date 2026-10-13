package Datatypes;

import java.io.Serializable;
import java.util.Date;

public class DtEdicionCurso implements Serializable {
    private String nombre;
    private Date fechaI;
    private Date fechaF;
    private int cupo;
    private Date fechaPub;
    private String nombreCurso; // Referencia al curso por nombre

    public DtEdicionCurso() {
    }

    public DtEdicionCurso(String nombre, Date fechaI, Date fechaF, int cupo, Date fechaPub, String nombreCurso) {
        this.nombre = nombre;
        this.fechaI = fechaI;
        this.fechaF = fechaF;
        this.cupo = cupo;
        this.fechaPub = fechaPub;
        this.nombreCurso = nombreCurso;
    }

    // Getters
    public String getNombre() { return nombre; }
    public Date getFechaI() { return fechaI; }
    public Date getFechaF() { return fechaF; }
    public int getCupo() { return cupo; }
    public Date getFechaPub() { return fechaPub; }
    public String getNombreCurso() { return nombreCurso; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaI(Date fechaI) { this.fechaI = fechaI; }
    public void setFechaF(Date fechaF) { this.fechaF = fechaF; }
    public void setCupo(int cupo) { this.cupo = cupo; }
    public void setFechaPub(Date fechaPub) { this.fechaPub = fechaPub; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }
}
