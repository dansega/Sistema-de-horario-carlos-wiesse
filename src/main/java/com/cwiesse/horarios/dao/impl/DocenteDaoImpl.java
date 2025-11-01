package com.cwiesse.horarios.dao.impl;

import com.cwiesse.horarios.dao.DocenteDao;
import com.cwiesse.horarios.model.Docente;
import com.cwiesse.horarios.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de DocenteDao.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class DocenteDaoImpl implements DocenteDao {
    
    private static final Logger logger = LoggerFactory.getLogger(DocenteDaoImpl.class);
    private final DBConnection dbConnection;
    
    public DocenteDaoImpl() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    @Override
    public Optional<Docente> buscarPorId(Integer id) {
        String sql = "SELECT * FROM docente WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Docente docente = mapResultSetToDocente(rs);
                logger.debug("Docente encontrado con ID: {}", id);
                return Optional.of(docente);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar docente por ID: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public Optional<Docente> buscarPorDni(String dni) {
        String sql = "SELECT * FROM docente WHERE dni = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dni);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Docente docente = mapResultSetToDocente(rs);
                logger.debug("Docente encontrado con DNI: {}", dni);
                return Optional.of(docente);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar docente por DNI: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Docente> listarTodos() {
        String sql = "SELECT * FROM docente ORDER BY apellido_paterno, apellido_materno, nombre";
        List<Docente> docentes = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                docentes.add(mapResultSetToDocente(rs));
            }
            
            logger.debug("Se encontraron {} docentes", docentes.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar docentes: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return docentes;
    }
    
    @Override
    public List<Docente> listarActivos() {
        String sql = "SELECT * FROM docente WHERE estado = 1 ORDER BY apellido_paterno, apellido_materno, nombre";
        List<Docente> docentes = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                docentes.add(mapResultSetToDocente(rs));
            }
            
            logger.debug("Se encontraron {} docentes activos", docentes.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar docentes activos: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return docentes;
    }
    
    @Override
    public boolean insertar(Docente docente) {
        String sql = "INSERT INTO docente (dni, nombre, apellido_paterno, apellido_materno, email, telefono, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, docente.getDni());
            stmt.setString(2, docente.getNombre());
            stmt.setString(3, docente.getApellidoPaterno());
            stmt.setString(4, docente.getApellidoMaterno());
            stmt.setString(5, docente.getEmail());
            stmt.setString(6, docente.getTelefono());
            stmt.setBoolean(7, docente.isEstado());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    docente.setId(rs.getInt(1));
                }
                logger.info("Docente insertado: {} {}", docente.getNombre(), docente.getApellidoPaterno());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al insertar docente: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean actualizar(Docente docente) {
        String sql = "UPDATE docente SET dni = ?, nombre = ?, apellido_paterno = ?, apellido_materno = ?, " +
                     "email = ?, telefono = ?, estado = ? WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, docente.getDni());
            stmt.setString(2, docente.getNombre());
            stmt.setString(3, docente.getApellidoPaterno());
            stmt.setString(4, docente.getApellidoMaterno());
            stmt.setString(5, docente.getEmail());
            stmt.setString(6, docente.getTelefono());
            stmt.setBoolean(7, docente.isEstado());
            stmt.setInt(8, docente.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Docente actualizado: {}", docente.getNombreCompleto());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar docente: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM docente WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Docente eliminado con ID: {}", id);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar docente: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean desactivar(Integer id) {
        String sql = "UPDATE docente SET estado = 0 WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Docente desactivado con ID: {}", id);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al desactivar docente: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean existeDni(String dni) {
        String sql = "SELECT COUNT(*) FROM docente WHERE dni = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dni);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de DNI: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Docente
     */
    private Docente mapResultSetToDocente(ResultSet rs) throws SQLException {
        Docente docente = new Docente();
        docente.setId(rs.getInt("id"));
        docente.setDni(rs.getString("dni"));
        docente.setNombre(rs.getString("nombre"));
        docente.setApellidoPaterno(rs.getString("apellido_paterno"));
        docente.setApellidoMaterno(rs.getString("apellido_materno"));
        docente.setEmail(rs.getString("email"));
        docente.setTelefono(rs.getString("telefono"));
        docente.setEstado(rs.getBoolean("estado"));
        docente.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return docente;
    }
}