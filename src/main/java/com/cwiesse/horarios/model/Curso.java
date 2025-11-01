package com.cwiesse.horarios.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa un curso o materia académica.
 * Contiene información sobre nivel, grado y horas semanales.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class Curso implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Enumeración para el nivel
    public enum Nivel {
        PRIMARIA, SECUNDARIA
    }
    
    // Atributos
    private Integer id;
    private String nombre;
    private String codigo;
    private Nivel nivel;
    private Integer grado;
    private Integer horasSemanales;
    private String color;
    private boolean estado;
    
    // Constructor vacío
    public Curso() {
        this.estado = true;
        this.color = "#2563eb";
    }
    
    // Constructor con parámetros principales
    public Curso(String nombre, String codigo, Nivel nivel, Integer grado, Integer horasSemanales) {
        this();
        this.nombre = nombre;
        this.codigo = codigo;
        this.nivel = nivel;
        this.grado = grado;
        this.horasSemanales = horasSemanales;
    }
    
    // Constructor completo
    public Curso(Integer id, String nombre, String codigo, Nivel nivel, 
                 Integer grado, Integer horasSemanales, String color, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.nivel = nivel;
        this.grado = grado;
        this.horasSemanales = horasSemanales;
        this.color = color;
        this.estado = estado;
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
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public Nivel getNivel() {
        return nivel;
    }
    
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }
    
    public Integer getGrado() {
        return grado;
    }
    
    public void setGrado(Integer grado) {
        this.grado = grado;
    }
    
    public Integer getHorasSemanales() {
        return horasSemanales;
    }
    
    public void setHorasSemanales(Integer horasSemanales) {
        this.horasSemanales = horasSemanales;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public boolean isEstado() {
        return estado;
    }
    
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    // Método para obtener descripción completa
    public String getDescripcionCompleta() {
        return codigo + " - " + nombre + " (" + nivel + " " + grado + "°)";
    }
    
    // Método toString
    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", nivel=" + nivel +
                ", grado=" + grado +
                ", horasSemanales=" + horasSemanales +
                ", estado=" + estado +
                '}';
    }
    
    // Método equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return Objects.equals(codigo, curso.codigo);
    }
    
    // Método hashCode
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

sout  print 
        }
