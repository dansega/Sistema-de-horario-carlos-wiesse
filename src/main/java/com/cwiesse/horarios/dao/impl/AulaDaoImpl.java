package com.cwiesse.horarios.dao.impl;

import com.cwiesse.horarios.dao.AulaDao;
import com.cwiesse.horarios.model.Aula;
import com.cwiesse.horarios.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de AulaDao.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class AulaDaoImpl implements AulaDao {
    
    private static final Logger logger = LoggerFactory.getLogger(AulaDaoImpl.class);
    private final DBConnection dbConnection;
    
    public AulaDaoImpl() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    @Override
    public Optional<Aula> buscarPorId(Integer id) {
        String sql = "SELECT * FROM aula WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Aula aula = mapResultSetToAula(rs);
                logger.debug("Aula encontrada con ID: {}", id);
                return Optional.of(aula);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar aula por ID: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public Optional<Aula> buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM aula WHERE codigo = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigo);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Aula aula = mapResultSetToAula(rs);
                logger.debug("Aula encontrada con código: {}", codigo);
                return Optional.of(aula);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar aula por código: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Aula> listarTodas() {
        String sql = "SELECT * FROM aula ORDER BY edificio, piso, codigo";
        List<Aula> aulas = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                aulas.add(mapResultSetToAula(rs));
            }
            
            logger.debug("Se encontraron {} aulas", aulas.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar aulas: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return aulas;
    }
    
    @Override
    public List<Aula> listarActivas() {
        String sql = "SELECT * FROM aula WHERE estado = 1 ORDER BY edificio, piso, codigo";
        List<Aula> aulas = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                aulas.add(mapResultSetToAula(rs));
            }
            
            logger.debug("Se encontraron {} aulas activas", aulas.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar aulas activas: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return aulas;
    }
    
    @Override
    public boolean insertar(Aula aula) {
        String sql = "INSERT INTO aula (codigo, nombre, capacidad, piso, edificio, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, aula.getCodigo());
            stmt.setString(2, aula.getNombre());
            stmt.setInt(3, aula.getCapacidad());
            stmt.setInt(4, aula.getPiso());
            stmt.setString(5, aula.getEdificio());
            stmt.setBoolean(6, aula.isEstado());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    aula.setId(rs.getInt(1));
                }
                logger.info("Aula insertada: {}", aula.getCodigo());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al insertar aula: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean actualizar(Aula aula) {
        String sql = "UPDATE aula SET codigo = ?, nombre = ?, capacidad = ?, piso = ?, " +
                     "edificio = ?, estado = ? WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, aula.getCodigo());
            stmt.setString(2, aula.getNombre());
            stmt.setInt(3, aula.getCapacidad());
            stmt.setInt(4, aula.getPiso());
            stmt.setString(5, aula.getEdificio());
            stmt.setBoolean(6, aula.isEstado());
            stmt.setInt(7, aula.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Aula actualizada: {}", aula.getCodigo());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar aula: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM aula WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Aula eliminada con ID: {}", id);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar aula: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean existeCodigo(String codigo) {
        String sql = "SELECT COUNT(*) FROM aula WHERE codigo = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigo);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de código: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Aula
     */
    private Aula mapResultSetToAula(ResultSet rs) throws SQLException {
        Aula aula = new Aula();
        aula.setId(rs.getInt("id"));
        aula.setCodigo(rs.getString("codigo"));
        aula.setNombre(rs.getString("nombre"));
        aula.setCapacidad(rs.getInt("capacidad"));
        aula.setPiso(rs.getInt("piso"));
        aula.setEdificio(rs.getString("edificio"));
        aula.setEstado(rs.getBoolean("estado"));
        return aula;
    }
}