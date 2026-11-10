package Datatypes;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EvaluacionDTO", propOrder = {"idInscripcion","nota"})
public class EvaluacionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idInscripcion;
    private Integer nota;

    public EvaluacionDTO() {
    }

    public EvaluacionDTO(Long idInscripcion, Integer nota) {
        this.idInscripcion = idInscripcion;
        this.nota = nota;
    }

    public Long getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}
