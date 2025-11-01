package com.cwiesse.horarios.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Pattern;

/**
 * Clase de utilidad para validaciones comunes usando Apache Commons Lang.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class Validation {
    
    private static final Logger logger = LoggerFactory.getLogger(Validation.class);
    
    // Expresiones regulares
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}$");
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^[0-9]{9}$");
    private static final Pattern CODIGO_PATTERN = Pattern.compile("^[A-Z0-9-]{3,20}$");
    
    /**
     * Valida que un string no sea nulo ni vacío
     */
    public static boolean isNotEmpty(String value) {
        return StringUtils.isNotBlank(value);
    }
    
    /**
     * Valida un DNI peruano (8 dígitos)
     */
    public static boolean isValidDNI(String dni) {
        if (StringUtils.isBlank(dni)) {
            logger.warn("DNI vacío");
            return false;
        }
        
        boolean valid = DNI_PATTERN.matcher(dni.trim()).matches();
        if (!valid) {
            logger.warn("DNI inválido: {}", dni);
        }
        return valid;
    }
    
    /**
     * Valida un email
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        
        boolean valid = EMAIL_PATTERN.matcher(email.trim()).matches();
        if (!valid) {
            logger.warn("Email inválido: {}", email);
        }
        return valid;
    }
    
    /**
     * Valida un teléfono peruano (9 dígitos)
     */
    public static boolean isValidTelefono(String telefono) {
        if (StringUtils.isBlank(telefono)) {
            return false;
        }
        
        return TELEFONO_PATTERN.matcher(telefono.trim()).matches();
    }
    
    /**
     * Valida un código (3-20 caracteres alfanuméricos con guiones)
     */
    public static boolean isValidCodigo(String codigo) {
        if (StringUtils.isBlank(codigo)) {
            return false;
        }
        
        return CODIGO_PATTERN.matcher(codigo.trim().toUpperCase()).matches();
    }
    
    /**
     * Valida que un número esté en un rango
     */
    public static boolean isInRange(Integer value, int min, int max) {
        if (value == null) {
            return false;
        }
        return value >= min && value <= max;
    }
    
    /**
     * Valida una capacidad de aula (1-50)
     */
    public static boolean isValidCapacidad(Integer capacidad) {
        return isInRange(capacidad, 1, 50);
    }
    
    /**
     * Valida un piso (1-5)
     */
    public static boolean isValidPiso(Integer piso) {
        return isInRange(piso, 1, 5);
    }
    
    /**
     * Valida horas semanales de un curso (1-10)
     */
    public static boolean isValidHorasSemanales(Integer horas) {
        return isInRange(horas, 1, 10);
    }
    
    /**
     * Valida un grado escolar (1-6)
     */
    public static boolean isValidGrado(Integer grado) {
        return isInRange(grado, 1, 6);
    }
    
    /**
     * Capitaliza un nombre (primera letra mayúscula)
     */
    public static String capitalizeName(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        return StringUtils.capitalize(name.trim().toLowerCase());
    }
    
    /**
     * Limpia y formatea un DNI
     */
    public static String formatDNI(String dni) {
        if (StringUtils.isBlank(dni)) {
            return dni;
        }
        return dni.trim().replaceAll("[^0-9]", "");
    }
}