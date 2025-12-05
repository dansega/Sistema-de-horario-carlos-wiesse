package com.cwiesse.horarios.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Modelo que representa un grado/sección del colegio.
 * Ejemplo: 1° A Primaria, 2° B Secundaria
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class Grado {
    
    private Integer id;
    private Nivel nivel;
    private Integer numero;
    private String seccion;
    private Integer aulaId;
    private boolean estado;
    private LocalDateTime fechaRegistro;
    
    // Relación con Aula (opcional)
    private Aula aula;
    
    /**
     * Enum para el nivel educativo
     */
    public enum Nivel {
        PRIMARIA("Primaria"),
        SECUNDARIA("Secundaria");
        
        private final String nombre;
        
        Nivel(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
    }
    
    // Constructores
    public Grado() {
        this.estado = true;
    }
    
    public Grado(Nivel nivel, Integer numero, String seccion) {
        this.nivel = nivel;
        this.numero = numero;
        this.seccion = seccion;
        this.estado = true;
    }
    
    public Grado(Nivel nivel, Integer numero, String seccion, Integer aulaId) {
        this.nivel = nivel;
        this.numero = numero;
        this.seccion = seccion;
        this.aulaId = aulaId;
        this.estado = true;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Nivel getNivel() {
        return nivel;
    }
    
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }
    
    public Integer getNumero() {
        return numero;
    }
    
    public void setNumero(Integer numero) {
        this.numero = numero;
    }
    
    public String getSeccion() {
        return seccion;
    }
    
    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
    
    public Integer getAulaId() {
        return aulaId;
    }
    
    public void setAulaId(Integer aulaId) {
        this.aulaId = aulaId;
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
    
    public Aula getAula() {
        return aula;
    }
    
    public void setAula(Aula aula) {
        this.aula = aula;
    }
    
    /**
     * Obtiene el nombre completo del grado
     * Ejemplo: "1° A - Primaria"
     */
    public String getNombreCompleto() {
        return numero + "° " + seccion + " - " + nivel.getNombre();
    }
    
    /**
     * Obtiene el nombre corto del grado
     * Ejemplo: "1° A"
     */
    public String getNombreCorto() {
        return numero + "° " + seccion;
    }
    
    @Override
    public String toString() {
        return "Grado{" +
                "id=" + id +
                ", nivel=" + nivel +
                ", numero=" + numero +
                ", seccion='" + seccion + '\'' +
                ", aulaId=" + aulaId +
                ", estado=" + estado +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grado grado = (Grado) o;
        return nivel == grado.nivel && 
               Objects.equals(numero, grado.numero) && 
               Objects.equals(seccion, grado.seccion);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nivel, numero, seccion);
    }
}