
package Datatypes;

public class CursoTendenciaDTO {
    private String nombre;
    private int visitas;

    public CursoTendenciaDTO() {}
    public CursoTendenciaDTO(String nombre, int visitas) {
        this.nombre = nombre;
        this.visitas = visitas;
    }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getVisitas() { return visitas; }
    public void setVisitas(int visitas) { this.visitas = visitas; }
}
