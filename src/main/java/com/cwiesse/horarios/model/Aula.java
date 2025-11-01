package com.cwiesse.horarios.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa un aula o espacio educativo del colegio.
 * Contiene información sobre capacidad y ubicación.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class Aula implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private Integer id;
    private String codigo;
    private String nombre;
    private Integer capacidad;
    private Integer piso;
    private String edificio;
    private boolean estado;
    
    // Constructor vacío
    public Aula() {
        this.estado = true;
    }
    
    // Constructor con parámetros principales
    public Aula(String codigo, String nombre, Integer capacidad, Integer piso) {
        this();
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.piso = piso;
    }
    
    // Constructor completo
    public Aula(Integer id, String codigo, String nombre, Integer capacidad, 
                Integer piso, String edificio, boolean estado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.piso = piso;
        this.edificio = edificio;
        this.estado = estado;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Integer getCapacidad() {
        return capacidad;
    }
    
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
    
    public Integer getPiso() {
        return piso;
    }
    
    public void setPiso(Integer piso) {
        this.piso = piso;
    }
    
    public String getEdificio() {
        return edificio;
    }
    
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }
    
    public boolean isEstado() {
        return estado;
    }
    
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    // Método para obtener descripción completa
    public String getDescripcionCompleta() {
        return codigo + " - " + nombre + " (Capacidad: " + capacidad + ")";
    }
    
    // Método toString
    @Override
    public String toString() {
        return "Aula{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", capacidad=" + capacidad +
                ", piso=" + piso +
                ", edificio='" + edificio + '\'' +
                ", estado=" + estado +
                '}';
    }
    
    // Método equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aula aula = (Aula) o;
        return Objects.equals(codigo, aula.codigo);
    }
    
    // Método hashCode
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}