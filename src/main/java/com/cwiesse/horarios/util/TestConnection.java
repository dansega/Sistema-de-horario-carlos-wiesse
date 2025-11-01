package com.cwiesse.horarios.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clase para probar la conexión a MySQL
 */
public class TestConnection {
    
    public static void main(String[] args) {
        System.out.println("=== TEST DE CONEXIÓN A MYSQL ===\n");
        
        DBConnection dbConnection = DBConnection.getInstance();
        Connection conn = null;
        
        try {
            // Intentar conectar
            System.out.println("Intentando conectar a la base de datos...");
            conn = dbConnection.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ CONEXIÓN EXITOSA!\n");
                
                // Probar una consulta simple
                System.out.println("Ejecutando consulta de prueba...");
                Statement stmt = conn.createStatement();
                
                // Contar docentes
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM docente");
                if (rs.next()) {
                    int total = rs.getInt("total");
                    System.out.println("✅ Total de docentes en BD: " + total);
                }
                rs.close();
                
                // Contar aulas
                rs = stmt.executeQuery("SELECT COUNT(*) as total FROM aula");
                if (rs.next()) {
                    int total = rs.getInt("total");
                    System.out.println("✅ Total de aulas en BD: " + total);
                }
                rs.close();
                
                // Contar cursos
                rs = stmt.executeQuery("SELECT COUNT(*) as total FROM curso");
                if (rs.next()) {
                    int total = rs.getInt("total");
                    System.out.println("✅ Total de cursos en BD: " + total);
                }
                rs.close();
                
                // Contar horarios
                rs = stmt.executeQuery("SELECT COUNT(*) as total FROM horario");
                if (rs.next()) {
                    int total = rs.getInt("total");
                    System.out.println("✅ Total de horarios en BD: " + total);
                }
                rs.close();
                
                stmt.close();
                
                System.out.println("\n✅ ¡TODO FUNCIONA CORRECTAMENTE!");
                
            } else {
                System.out.println("❌ Error: No se pudo establecer conexión");
            }
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar conexión
            if (conn != null) {
                dbConnection.closeConnection(conn);
                System.out.println("\n✅ Conexión cerrada correctamente");
            }
        }
    }
}