package Datatypes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListadoCursosDTO")
public class ListadoCursosDTO {

    private List<CursoListadoDTO> cursos = new ArrayList<>();
    private List<String> cursosEditables = new ArrayList<>();

    public ListadoCursosDTO() {
    }

    public List<CursoListadoDTO> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoListadoDTO> cursos) {
        this.cursos = cursos;
    }

    public List<String> getCursosEditables() {
        return cursosEditables;
    }

    public void setCursosEditables(List<String> cursosEditables) {
        this.cursosEditables = cursosEditables;
    }
}