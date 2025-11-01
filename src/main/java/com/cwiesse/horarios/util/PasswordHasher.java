package com.cwiesse.horarios.util;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de utilidad para hashear y verificar contraseñas usando BCrypt.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class PasswordHasher {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordHasher.class);
    private static final int WORK_FACTOR = 10;
    
    /**
     * Hashea una contraseña en texto plano usando BCrypt
     * 
     * @param plainPassword Contraseña en texto plano
     * @return Hash de la contraseña
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            logger.warn("Intento de hashear contraseña vacía");
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        
        try {
            String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt(WORK_FACTOR));
            logger.debug("Contraseña hasheada correctamente");
            return hash;
        } catch (Exception e) {
            logger.error("Error al hashear contraseña: {}", e.getMessage());
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }
    
    /**
     * Verifica si una contraseña en texto plano coincide con el hash
     * 
     * @param plainPassword Contraseña en texto plano
     * @param hashedPassword Hash de la contraseña
     * @return true si coinciden, false si no
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            logger.warn("Intento de verificar contraseña con valores nulos");
            return false;
        }
        
        try {
            boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
            if (matches) {
                logger.debug("Contraseña verificada correctamente");
            } else {
                logger.debug("Contraseña no coincide");
            }
            return matches;
        } catch (Exception e) {
            logger.error("Error al verificar contraseña: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Método main para testing (crear hashes manualmente)
     */
    public static void main(String[] args) {
        // Ejemplo de uso
        String password = "admin123";
        String hash = hashPassword(password);
        
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("Verificación: " + verifyPassword(password, hash));
        
        // Ejemplo para crear hashes de usuarios demo
        System.out.println("\n--- Hashes para usuarios demo ---");
        System.out.println("admin123: " + hashPassword("admin123"));
        System.out.println("doc123: " + hashPassword("doc123"));
    }
}