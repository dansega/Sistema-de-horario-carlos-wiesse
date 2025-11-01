package com.cwiesse.horarios.util;

public class TestVerifyHash {
    public static void main(String[] args) {
        String password = "admin123";
        String hash = "$2a$10$IcWw.yfJycQiqGC/6oHp3OOykT9t6kECpWDWc/S0h/XNvsiYGWcey";
        
        System.out.println("Testing password: " + password);
        System.out.println("Against hash: " + hash);
        
        boolean resultado = PasswordHasher.verifyPassword(password, hash);
        
        System.out.println("Resultado: " + resultado);
        
        if (resultado) {
            System.out.println("✅ EL HASH ES CORRECTO");
        } else {
            System.out.println("❌ EL HASH NO FUNCIONA");
        }
    }
}