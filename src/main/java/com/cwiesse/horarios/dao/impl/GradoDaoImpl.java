package com.cwiesse.horarios.dao.impl;

import com.cwiesse.horarios.dao.GradoDao;
import com.cwiesse.horarios.model.Grado;
import com.cwiesse.horarios.model.Aula;
import com.cwiesse.horarios.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de GradoDao.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class GradoDaoImpl implements GradoDao {
    
    private static final Logger logger = LoggerFactory.getLogger(GradoDaoImpl.class);
    private final DBConnection dbConnection;
    
    public GradoDaoImpl() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    @Override
    public Optional<Grado> buscarPorId(Integer id) {
        String sql = "SELECT g.*, a.codigo as aula_codigo, a.nombre as aula_nombre " +
                    "FROM grado g " +
                    "LEFT JOIN aula a ON g.aula_id = a.id " +
                    "WHERE g.id = ?";
        
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Grado grado = mapResultSetToGrado(rs);
                logger.debug("Grado encontrado con ID: {}", id);
                return Optional.of(grado);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar grado por ID: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Grado> listarTodos() {
        String sql = "SELECT g.*, a.codigo as aula_codigo, a.nombre as aula_nombre " +
                    "FROM grado g " +
                    "LEFT JOIN aula a ON g.aula_id = a.id " +
                    "ORDER BY g.nivel, g.numero, g.seccion";
        
        List<Grado> grados = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                grados.add(mapResultSetToGrado(rs));
            }
            
            logger.debug("Se encontraron {} grados", grados.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar grados: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return grados;
    }
    
    @Override
    public List<Grado> listarActivos() {
        String sql = "SELECT g.*, a.codigo as aula_codigo, a.nombre as aula_nombre " +
                    "FROM grado g " +
                    "LEFT JOIN aula a ON g.aula_id = a.id " +
                    "WHERE g.estado = TRUE " +
                    "ORDER BY g.nivel, g.numero, g.seccion";
        
        List<Grado> grados = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                grados.add(mapResultSetToGrado(rs));
            }
            
            logger.debug("Se encontraron {} grados activos", grados.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar grados activos: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return grados;
    }
    
    @Override
    public List<Grado> listarPorNivel(Grado.Nivel nivel) {
        String sql = "SELECT g.*, a.codigo as aula_codigo, a.nombre as aula_nombre " +
                    "FROM grado g " +
                    "LEFT JOIN aula a ON g.aula_id = a.id " +
                    "WHERE g.nivel = ? AND g.estado = TRUE " +
                    "ORDER BY g.numero, g.seccion";
        
        List<Grado> grados = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nivel.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                grados.add(mapResultSetToGrado(rs));
            }
            
            logger.debug("Se encontraron {} grados de {}", grados.size(), nivel);
            
        } catch (SQLException e) {
            logger.error("Error al listar grados por nivel: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return grados;
    }
    
    @Override
    public boolean insertar(Grado grado) {
        String sql = "INSERT INTO grado (nivel, numero, seccion, aula_id, estado) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, grado.getNivel().name());
            stmt.setInt(2, grado.getNumero());
            stmt.setString(3, grado.getSeccion());
            
            if (grado.getAulaId() != null) {
                stmt.setInt(4, grado.getAulaId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setBoolean(5, grado.isEstado());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    grado.setId(rs.getInt(1));
                }
                logger.info("Grado insertado: {}", grado.getNombreCompleto());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al insertar grado: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean actualizar(Grado grado) {
        String sql = "UPDATE grado SET nivel = ?, numero = ?, seccion = ?, aula_id = ?, estado = ? " +
                     "WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, grado.getNivel().name());
            stmt.setInt(2, grado.getNumero());
            stmt.setString(3, grado.getSeccion());
            
            if (grado.getAulaId() != null) {
                stmt.setInt(4, grado.getAulaId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setBoolean(5, grado.isEstado());
            stmt.setInt(6, grado.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Grado actualizado: ID {}", grado.getId());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar grado: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) {
        String sql = "UPDATE grado SET estado = FALSE WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Grado eliminado (soft delete) con ID: {}", id);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar grado: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean existeGrado(Grado.Nivel nivel, Integer numero, String seccion) {
        String sql = "SELECT COUNT(*) FROM grado WHERE nivel = ? AND numero = ? AND seccion = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nivel.name());
            stmt.setInt(2, numero);
            stmt.setString(3, seccion);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de grado: {}", e.getMessage());
            return true; // Por seguridad, asumir que existe
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean existeGradoExceptoId(Grado.Nivel nivel, Integer numero, String seccion, Integer idExcluir) {
        String sql = "SELECT COUNT(*) FROM grado WHERE nivel = ? AND numero = ? AND seccion = ? AND id != ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nivel.name());
            stmt.setInt(2, numero);
            stmt.setString(3, seccion);
            stmt.setInt(4, idExcluir);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de grado exceptuando ID: {}", e.getMessage());
            return true; // Por seguridad, asumir que existe
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Grado
     */
    private Grado mapResultSetToGrado(ResultSet rs) throws SQLException {
        Grado grado = new Grado();
        
        grado.setId(rs.getInt("id"));
        
        String nivelStr = rs.getString("nivel");
        grado.setNivel(Grado.Nivel.valueOf(nivelStr));
        
        grado.setNumero(rs.getInt("numero"));
        grado.setSeccion(rs.getString("seccion"));
        
        Integer aulaId = rs.getInt("aula_id");
        if (!rs.wasNull()) {
            grado.setAulaId(aulaId);
            
            // Crear objeto Aula si existe
            String aulaCodigo = rs.getString("aula_codigo");
            if (aulaCodigo != null) {
                Aula aula = new Aula();
                aula.setId(aulaId);
                aula.setCodigo(aulaCodigo);
                aula.setNombre(rs.getString("aula_nombre"));
                grado.setAula(aula);
            }
        }
        
        grado.setEstado(rs.getBoolean("estado"));
        
        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            grado.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }
        
        return grado;
    }
}