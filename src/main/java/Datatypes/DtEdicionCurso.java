package Datatypes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DtEdicionCurso implements Serializable {
    private String nombre;
    private LocalDate fechaI;
    private LocalDate fechaF;
    private int cupo;
    private LocalDate fechaPub;
    private String nombreCurso;
    private List<String> nombresDocentes = new ArrayList<>();
    private List<String> nombresInscriptos = new ArrayList<>();

    public DtEdicionCurso() {
    }

    public DtEdicionCurso(String nombre, LocalDate fechaI, LocalDate fechaF, int cupo, LocalDate fechaPub, String nombreCurso) {
        this.nombre = nombre;
        this.fechaI = fechaI;
        this.fechaF = fechaF;
        this.cupo = cupo;
        this.fechaPub = fechaPub;
        this.nombreCurso = nombreCurso;
    }

    // Getters
    public String getNombre() { return nombre; }
    public LocalDate getFechaI() { return fechaI; }
    public LocalDate getFechaF() { return fechaF; }
    public int getCupo() { return cupo; }
    public LocalDate getFechaPub() { return fechaPub; }
    public String getNombreCurso() { return nombreCurso; }
    public List<String> getNombresDocentes() { return nombresDocentes; }
    public List<String> getNombresInscriptos() { return nombresInscriptos; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaI(LocalDate fechaI) { this.fechaI = fechaI; }
    public void setFechaF(LocalDate fechaF) { this.fechaF = fechaF; }
    public void setCupo(int cupo) { this.cupo = cupo; }
    public void setFechaPub(LocalDate fechaPub) { this.fechaPub = fechaPub; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }
    public void setNombresDocentes(List<String> nombresDocentes) { this.nombresDocentes = nombresDocentes; }
    public void setNombresInscriptos(List<String> nombresInscriptos) { this.nombresInscriptos = nombresInscriptos; }
}
