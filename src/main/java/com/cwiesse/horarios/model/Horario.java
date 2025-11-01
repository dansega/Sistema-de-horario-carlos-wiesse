package com.cwiesse.horarios.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase que representa una sesión de horario escolar.
 * Relaciona docente, aula, curso, día y horario.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class Horario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Enumeración para los días
    public enum Dia {
        LUN("Lunes"),
        MAR("Martes"),
        MIE("Miércoles"),
        JUE("Jueves"),
        VIE("Viernes");
        
        private final String nombre;
        
        Dia(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
    }
    
    // Atributos
    private Integer id;
    private Integer docenteId;
    private Integer aulaId;
    private Integer cursoId;
    private Dia dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private LocalDateTime fechaCreacion;
    
    // Objetos relacionados
    private Docente docente;
    private Aula aula;
    private Curso curso;
    
    // Constructor vacío
    public Horario() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Constructor con IDs
    public Horario(Integer docenteId, Integer aulaId, Integer cursoId, 
                   Dia dia, LocalTime horaInicio, LocalTime horaFin) {
        this();
        this.docenteId = docenteId;
        this.aulaId = aulaId;
        this.cursoId = cursoId;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
    
    // Constructor completo
    public Horario(Integer id, Integer docenteId, Integer aulaId, Integer cursoId,
                   Dia dia, LocalTime horaInicio, LocalTime horaFin) {
        this.id = id;
        this.docenteId = docenteId;
        this.aulaId = aulaId;
        this.cursoId = cursoId;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getDocenteId() {
        return docenteId;
    }
    
    public void setDocenteId(Integer docenteId) {
        this.docenteId = docenteId;
    }
    
    public Integer getAulaId() {
        return aulaId;
    }
    
    public void setAulaId(Integer aulaId) {
        this.aulaId = aulaId;
    }
    
    public Integer getCursoId() {
        return cursoId;
    }
    
    public void setCursoId(Integer cursoId) {
        this.cursoId = cursoId;
    }
    
    public Dia getDia() {
        return dia;
    }
    
    public void setDia(Dia dia) {
        this.dia = dia;
    }
    
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    
    public LocalTime getHoraFin() {
        return horaFin;
    }
    
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Docente getDocente() {
        return docente;
    }
    
    public void setDocente(Docente docente) {
        this.docente = docente;
    }
    
    public Aula getAula() {
        return aula;
    }
    
    public void setAula(Aula aula) {
        this.aula = aula;
    }
    
    public Curso getCurso() {
        return curso;
    }
    
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    // Método para validar horario
    public boolean esHorarioValido() {
        return horaInicio != null && horaFin != null && horaInicio.isBefore(horaFin);
    }
    
    // Método para calcular duración
    public long getDuracionMinutos() {
        if (horaInicio != null && horaFin != null) {
            return java.time.Duration.between(horaInicio, horaFin).toMinutes();
        }
        return 0;
    }
    
    // Método toString
    @Override
    public String toString() {
        return "Horario{" +
                "id=" + id +
                ", dia=" + dia +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                '}';
    }
    
    // Método equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horario horario = (Horario) o;
        return Objects.equals(id, horario.id);
    }
    
    // Método hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
//test
