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
    public Optional<Curso> buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM curso WHERE codigo = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigo);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Curso curso = mapResultSetToCurso(rs);
                logger.debug("Curso encontrado con código: {}", codigo);
                return Optional.of(curso);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar curso por código: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Curso> listarTodos() {
        String sql = "SELECT * FROM curso ORDER BY nivel, grado, nombre";
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
        String sql = "SELECT * FROM curso WHERE estado = 1 ORDER BY nivel, grado, nombre";
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
        String sql = "INSERT INTO curso (codigo, nombre, nivel, grado, horas_semanales, color, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, curso.getCodigo());
            stmt.setString(2, curso.getNombre());
            stmt.setString(3, curso.getNivel().name());
            stmt.setInt(4, curso.getGrado());
            stmt.setInt(5, curso.getHorasSemanales());
            stmt.setString(6, curso.getColor());
            stmt.setBoolean(7, curso.isEstado());
            
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
        String sql = "UPDATE curso SET codigo = ?, nombre = ?, nivel = ?, grado = ?, " +
                     "horas_semanales = ?, color = ?, estado = ? WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, curso.getCodigo());
            stmt.setString(2, curso.getNombre());
            stmt.setString(3, curso.getNivel().name());
            stmt.setInt(4, curso.getGrado());
            stmt.setInt(5, curso.getHorasSemanales());
            stmt.setString(6, curso.getColor());
            stmt.setBoolean(7, curso.isEstado());
            stmt.setInt(8, curso.getId());
            
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
        String sql = "DELETE FROM curso WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Curso eliminado con ID: {}", id);
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
    public boolean existeCodigo(String codigo) {
        String sql = "SELECT COUNT(*) FROM curso WHERE codigo = ?";
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
     * Mapea un ResultSet a un objeto Curso
     */
    private Curso mapResultSetToCurso(ResultSet rs) throws SQLException {
        Curso curso = new Curso();
        curso.setId(rs.getInt("id"));
        curso.setCodigo(rs.getString("codigo"));
        curso.setNombre(rs.getString("nombre"));
        
        String nivelStr = rs.getString("nivel");
        curso.setNivel(Curso.Nivel.valueOf(nivelStr));
        
        curso.setGrado(rs.getInt("grado"));
        curso.setHorasSemanales(rs.getInt("horas_semanales"));
        curso.setColor(rs.getString("color"));
        curso.setEstado(rs.getBoolean("estado"));
        
        return curso;
    }
}