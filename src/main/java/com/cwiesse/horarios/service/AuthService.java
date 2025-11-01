package com.cwiesse.horarios.service;

import com.cwiesse.horarios.dao.UsuarioDao;
import com.cwiesse.horarios.dao.impl.UsuarioDaoImpl;
import com.cwiesse.horarios.model.Usuario;
import com.cwiesse.horarios.util.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Servicio de autenticación de usuarios.
 * Maneja login y validación de credenciales.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UsuarioDao usuarioDao;
    
    public AuthService() {
        this.usuarioDao = new UsuarioDaoImpl();
    }
    
    /**
     * Autentica un usuario con username y password
     * 
     * @param username Username del usuario
     * @param password Password en texto plano
     * @return Optional con el usuario si las credenciales son correctas
     */
    public Optional<Usuario> autenticar(String username, String password) {
        logger.debug("Intentando autenticar usuario: {}", username);
        
        // Validaciones básicas
        if (username == null || username.trim().isEmpty()) {
            logger.warn("Username vacío");
            return Optional.empty();
        }
        
        if (password == null || password.trim().isEmpty()) {
            logger.warn("Password vacío");
            return Optional.empty();
        }
        
        // Buscar usuario
        Optional<Usuario> usuarioOpt = usuarioDao.buscarPorUsername(username.trim());
        
        if (usuarioOpt.isEmpty()) {
            logger.warn("Usuario no encontrado: {}", username);
            return Optional.empty();
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar si está activo
        if (!usuario.isActivo()) {
            logger.warn("Usuario inactivo: {}", username);
            return Optional.empty();
        }
        
        // Verificar password
        boolean passwordCorrecta = PasswordHasher.verifyPassword(password, usuario.getPasswordHash());
        
        if (passwordCorrecta) {
            logger.info("Usuario autenticado exitosamente: {}", username);
            return Optional.of(usuario);
        } else {
            logger.warn("Password incorrecta para usuario: {}", username);
            return Optional.empty();
        }
    }
    
    /**
     * Verifica si un usuario es administrador
     */
    public boolean esAdmin(Usuario usuario) {
        return usuario != null && usuario.esAdmin();
    }
    
    /**
     * Verifica si un usuario es docente
     */
    public boolean esDocente(Usuario usuario) {
        return usuario != null && usuario.esDocente();
    }
}