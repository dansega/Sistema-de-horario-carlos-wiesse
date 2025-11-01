package com.cwiesse.horarios.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase que representa a un docente del colegio.
 * Contiene información personal y de contacto.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class Docente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos
    private Integer id;
    private String dni;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String telefono;
    private boolean estado;
    private LocalDateTime fechaRegistro;
    
    // Constructor vacío
    public Docente() {
        this.estado = true;
        this.fechaRegistro = LocalDateTime.now();
    }
    
    // Constructor con parámetros principales
    public Docente(String dni, String nombre, String apellidoPaterno, String apellidoMaterno) {
        this();
        this.dni = dni;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }
    
    // Constructor completo
    public Docente(Integer id, String dni, String nombre, String apellidoPaterno, 
                   String apellidoMaterno, String email, String telefono, boolean estado) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
        this.telefono = telefono;
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
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }
    
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }
    
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }
    
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
    
    // Método para obtener nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }
    
    // Método toString
    @Override
    public String toString() {
        return "Docente{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", nombre='" + getNombreCompleto() + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                '}';
    }
    
    // Método equals (para comparar docentes)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Docente docente = (Docente) o;
        return Objects.equals(dni, docente.dni);
    }
    
    // Método hashCode
    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }
}