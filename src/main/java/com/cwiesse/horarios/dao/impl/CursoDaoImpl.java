package com.cwiesse.horarios.dao.impl;

import com.cwiesse.horarios.dao.CursoDao;
import com.cwiesse.horarios.model.Curso;
import com.cwiesse.horarios.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de CursoDao.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class CursoDaoImpl implements CursoDao {
    
    private static final Logger logger = LoggerFactory.getLogger(CursoDaoImpl.class);
    private final DBConnection dbConnection;
    
    public CursoDaoImpl() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    @Override
    public Optional<Curso> buscarPorId(Integer id) {
        String sql = "SELECT * FROM curso WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Curso curso = mapResultSetToCurso(rs);
                logger.debug("Curso encontrado con ID: {}", id);
                return Optional.of(curso);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar curso por ID: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public Optional<Curso> buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM curso WHERE nombre = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Curso curso = mapResultSetToCurso(rs);
                logger.debug("Curso encontrado con nombre: {}", nombre);
                return Optional.of(curso);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar curso por nombre: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Curso> listarTodos() {
        String sql = "SELECT * FROM curso ORDER BY nombre";
        List<Curso> cursos = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                cursos.add(mapResultSetToCurso(rs));
            }
            
            logger.debug("Se encontraron {} cursos", cursos.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar cursos: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return cursos;
    }
    
    @Override
    public List<Curso> listarActivos() {
        String sql = "SELECT * FROM curso WHERE estado = 1 ORDER BY nombre";
        List<Curso> cursos = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                cursos.add(mapResultSetToCurso(rs));
            }
            
            logger.debug("Se encontraron {} cursos activos", cursos.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar cursos activos: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return cursos;
    }
    
    @Override
    public boolean insertar(Curso curso) {
        String sql = "INSERT INTO curso (nombre, descripcion, horas_semanales, estado) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, curso.getNombre());
            stmt.setString(2, curso.getDescripcion());
            stmt.setInt(3, curso.getHorasSemanales());
            stmt.setBoolean(4, curso.isEstado());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    curso.setId(rs.getInt(1));
                }
                logger.info("Curso insertado: {}", curso.getNombre());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al insertar curso: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean actualizar(Curso curso) {
        String sql = "UPDATE curso SET nombre = ?, descripcion = ?, horas_semanales = ?, estado = ? WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, curso.getNombre());
            stmt.setString(2, curso.getDescripcion());
            stmt.setInt(3, curso.getHorasSemanales());
            stmt.setBoolean(4, curso.isEstado());
            stmt.setInt(5, curso.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Curso actualizado: {}", curso.getNombre());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar curso: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) {
        // Soft delete: cambiar estado a false
        String sql = "UPDATE curso SET estado = 0 WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Curso desactivado con ID: {}", id);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar curso: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM curso WHERE nombre = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de nombre: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean existeNombreExceptoId(String nombre, Integer id) {
        String sql = "SELECT COUNT(*) FROM curso WHERE nombre = ? AND id != ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setInt(2, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de nombre exceptuando ID: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Curso
     */
    private Curso mapResultSetToCurso(ResultSet rs) throws SQLException {
        Curso curso = new Curso();
        curso.setId(rs.getInt("id"));
        curso.setNombre(rs.getString("nombre"));
        curso.setDescripcion(rs.getString("descripcion"));
        curso.setHorasSemanales(rs.getInt("horas_semanales"));
        curso.setEstado(rs.getBoolean("estado"));
        
        // Mapear fecha_registro si existe
        Timestamp timestamp = rs.getTimestamp("fecha_registro");
        if (timestamp != null) {
            curso.setFechaRegistro(timestamp.toLocalDateTime());
        }
        
        return curso;
    }
}