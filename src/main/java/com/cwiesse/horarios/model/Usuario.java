package com.cwiesse.horarios.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase que representa un usuario del sistema.
 * Utilizada para autenticación y autorización.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Enumeración para roles
    public enum Rol {
        ADMIN("Administrador"),
        DOCENTE("Docente");
        
        private final String nombre;
        
        Rol(String nombre) {
            this.nombre = nombre;
        }
        
        public String getNombre() {
            return nombre;
        }
    }
    
    // Atributos
    private Integer id;
    private String username;
    private String passwordHash;
    private Rol rol;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    
    // Constructor vacío
    public Usuario() {
        this.activo = true;
        this.rol = Rol.ADMIN;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Constructor con parámetros
    public Usuario(String username, String passwordHash, Rol rol) {
        this();
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }
    
    // Constructor completo
    public Usuario(Integer id, String username, String passwordHash, Rol rol, boolean activo) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.activo = activo;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    // Métodos de utilidad
    public boolean esAdmin() {
        return this.rol == Rol.ADMIN;
    }
    
    public boolean esDocente() {
        return this.rol == Rol.DOCENTE;
    }
    
    // Método toString
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", rol=" + rol +
                ", activo=" + activo +
                '}';
    }
    
    // Método equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(username, usuario.username);
    }
    
    // Método hashCode
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}