package com.cwiesse.horarios.util;

import com.cwiesse.horarios.model.Usuario;
import com.cwiesse.horarios.service.AuthService;
import java.util.Optional;

/**
 * Clase para probar el servicio de autenticación
 */
public class TestAuthService {
    
    public static void main(String[] args) {
        System.out.println("=== TEST DE AUTENTICACIÓN ===\n");
        
        AuthService authService = new AuthService();
        
        // Test 1: Login correcto con ADMIN
        System.out.println("1. LOGIN ADMIN (correcto):");
        System.out.println("===========================");
        System.out.println("Username: admin");
        System.out.println("Password: admin123");
        
        Optional<Usuario> usuarioOpt = authService.autenticar("admin", "admin123");
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("✅ LOGIN EXITOSO!");
            System.out.println("   Usuario: " + usuario.getUsername());
            System.out.println("   Rol: " + usuario.getRol().getNombre());
            System.out.println("   Es Admin: " + (authService.esAdmin(usuario) ? "Sí" : "No"));
        } else {
            System.out.println("❌ LOGIN FALLIDO");
        }
        System.out.println();
        
        // Test 2: Login correcto con DOCENTE
        System.out.println("2. LOGIN DOCENTE (correcto):");
        System.out.println("=============================");
        System.out.println("Username: jperez");
        System.out.println("Password: doc123");
        
        usuarioOpt = authService.autenticar("jperez", "doc123");
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("✅ LOGIN EXITOSO!");
            System.out.println("   Usuario: " + usuario.getUsername());
            System.out.println("   Rol: " + usuario.getRol().getNombre());
            System.out.println("   Es Docente: " + (authService.esDocente(usuario) ? "Sí" : "No"));
        } else {
            System.out.println("❌ LOGIN FALLIDO");
        }
        System.out.println();
        
        // Test 3: Login con contraseña incorrecta
        System.out.println("3. LOGIN con contraseña INCORRECTA:");
        System.out.println("====================================");
        System.out.println("Username: admin");
        System.out.println("Password: contraseña_mala");
        
        usuarioOpt = authService.autenticar("admin", "contraseña_mala");
        
        if (usuarioOpt.isPresent()) {
            System.out.println("✅ LOGIN EXITOSO (no debería pasar)");
        } else {
            System.out.println("❌ LOGIN FALLIDO (correcto - password incorrecta)");
        }
        System.out.println();
        
        // Test 4: Login con usuario inexistente
        System.out.println("4. LOGIN con usuario INEXISTENTE:");
        System.out.println("==================================");
        System.out.println("Username: usuario_falso");
        System.out.println("Password: cualquiera");
        
        usuarioOpt = authService.autenticar("usuario_falso", "cualquiera");
        
        if (usuarioOpt.isPresent()) {
            System.out.println("✅ LOGIN EXITOSO (no debería pasar)");
        } else {
            System.out.println("❌ LOGIN FALLIDO (correcto - usuario no existe)");
        }
        System.out.println();
        
        // Test 5: Login con campos vacíos
        System.out.println("5. LOGIN con campos VACÍOS:");
        System.out.println("============================");
        
        usuarioOpt = authService.autenticar("", "");
        
        if (usuarioOpt.isPresent()) {
            System.out.println("✅ LOGIN EXITOSO (no debería pasar)");
        } else {
            System.out.println("❌ LOGIN FALLIDO (correcto - campos vacíos)");
        }
        System.out.println();
        
        System.out.println("✅ ¡TODAS LAS PRUEBAS DE AUTENTICACIÓN COMPLETADAS!");
        System.out.println("\n📋 Usuarios disponibles:");
        System.out.println("   - admin / admin123 (ADMIN)");
        System.out.println("   - jperez / doc123 (DOCENTE)");
        System.out.println("   - mgarcia / doc123 (DOCENTE)");
    }
}