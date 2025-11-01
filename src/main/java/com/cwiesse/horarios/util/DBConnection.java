package com.cwiesse.horarios.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase Singleton para gestionar la conexión a la base de datos MySQL.
 * Lee la configuración desde application.properties
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class DBConnection {
    
    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
    private static DBConnection instance;
    
    private String url;
    private String username;
    private String password;
    private String driver;
    
    // Constructor privado (Singleton)
    private DBConnection() {
        cargarConfiguracion();
    }
    
    /**
     * Obtiene la única instancia de DBConnection (Singleton)
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Carga la configuración desde application.properties
     */
    private void cargarConfiguracion() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            
            if (input == null) {
                logger.error("No se encontró application.properties");
                // Configuración por defecto
                this.url = "jdbc:mysql://localhost:3306/horarios_cwiesse?useSSL=false&serverTimezone=America/Lima";
                this.username = "root";
                this.password = "";
                this.driver = "com.mysql.cj.jdbc.Driver";
                return;
            }
            
            props.load(input);
            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");
            this.driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            
            logger.info("Configuración de BD cargada correctamente");
            
        } catch (IOException e) {
            logger.error("Error al cargar application.properties: {}", e.getMessage());
        }
    }
    
    /**
     * Obtiene una conexión a la base de datos
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            logger.debug("Conexión a BD establecida");
            return conn;
        } catch (ClassNotFoundException e) {
            logger.error("Driver MySQL no encontrado: {}", e.getMessage());
            throw new SQLException("Driver no encontrado", e);
        } catch (SQLException e) {
            logger.error("Error al conectar con BD: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Cierra una conexión de forma segura
     */
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                logger.debug("Conexión a BD cerrada");
            } catch (SQLException e) {
                logger.error("Error al cerrar conexión: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Verifica si la conexión está funcionando
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            logger.error("Test de conexión falló: {}", e.getMessage());
            return false;
        }
    }
}