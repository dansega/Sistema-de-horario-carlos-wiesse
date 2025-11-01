package com.cwiesse.horarios.dao.impl;

import com.cwiesse.horarios.dao.UsuarioDao;
import com.cwiesse.horarios.model.Usuario;
import com.cwiesse.horarios.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de UsuarioDao.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class UsuarioDaoImpl implements UsuarioDao {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDaoImpl.class);
    private final DBConnection dbConnection;
    
    // Constructor
    public UsuarioDaoImpl() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        String sql = "SELECT * FROM usuario WHERE username = ? AND activo = 1";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = mapResultSetToUsuario(rs);
                logger.debug("Usuario encontrado: {}", username);
                return Optional.of(usuario);
            }
            
            logger.debug("Usuario no encontrado: {}", username);
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar usuario por username: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = mapResultSetToUsuario(rs);
                logger.debug("Usuario encontrado con ID: {}", id);
                return Optional.of(usuario);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar usuario por ID: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario ORDER BY username";
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
            
            logger.debug("Se encontraron {} usuarios", usuarios.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar usuarios: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return usuarios;
    }
    
    @Override
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuario (username, password_hash, rol, activo) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPasswordHash());
            stmt.setString(3, usuario.getRol().name());
            stmt.setBoolean(4, usuario.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
                logger.info("Usuario insertado: {}", usuario.getUsername());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al insertar usuario: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET username = ?, password_hash = ?, rol = ?, activo = ? WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPasswordHash());
            stmt.setString(3, usuario.getRol().name());
            stmt.setBoolean(4, usuario.isActivo());
            stmt.setInt(5, usuario.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Usuario actualizado: {}", usuario.getUsername());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Usuario eliminado con ID: {}", id);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean existeUsername(String username) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE username = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de username: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Usuario
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPasswordHash(rs.getString("password_hash"));
        usuario.setRol(Usuario.Rol.valueOf(rs.getString("rol")));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        return usuario;
    }
}