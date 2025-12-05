package com.cwiesse.horarios.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase que representa un curso del colegio.
 * Un curso es genérico y puede dictarse en múltiples grados.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class Curso implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer horasSemanales;
    private boolean estado;
    private LocalDateTime fechaRegistro;
    
    // Constructor vacío
    public Curso() {
        this.estado = true;
        this.horasSemanales = 2;
        this.fechaRegistro = LocalDateTime.now();
    }
    
    // Constructor con parámetros principales
    public Curso(String nombre, String descripcion, Integer horasSemanales) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.horasSemanales = horasSemanales;
    }
    
    // Constructor completo
    public Curso(Integer id, String nombre, String descripcion, Integer horasSemanales, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.horasSemanales = horasSemanales;
        this.estado = estado;
        this.fechaRegistro = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Integer getHorasSemanales() {
        return horasSemanales;
    }
    
    public void setHorasSemanales(Integer horasSemanales) {
        this.horasSemanales = horasSemanales;
    }
    
    public boolean isEstado() {
        return estado;
    }
    
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    // Método toString
    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", horasSemanales=" + horasSemanales +
                ", estado=" + estado +
                '}';
    }
    
    // Método equals (para comparar cursos por nombre)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return Objects.equals(nombre, curso.nombre);
    }
    
    // Método hashCode
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}