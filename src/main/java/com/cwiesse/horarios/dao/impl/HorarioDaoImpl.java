package com.cwiesse.horarios.dao.impl;

import com.cwiesse.horarios.dao.HorarioDao;
import com.cwiesse.horarios.model.Horario;
import com.cwiesse.horarios.model.Docente;
import com.cwiesse.horarios.model.Aula;
import com.cwiesse.horarios.model.Curso;
import com.cwiesse.horarios.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de HorarioDao.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class HorarioDaoImpl implements HorarioDao {
    
    private static final Logger logger = LoggerFactory.getLogger(HorarioDaoImpl.class);
    private final DBConnection dbConnection;
    
    public HorarioDaoImpl() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    @Override
    public Optional<Horario> buscarPorId(Integer id) {
        String sql = "SELECT h.*, " +
                    "d.dni as docente_dni, d.nombre as docente_nombre, " +
                    "d.apellido_paterno as docente_ap_paterno, d.apellido_materno as docente_ap_materno, " +
                    "a.codigo as aula_codigo, a.nombre as aula_nombre, a.capacidad as aula_capacidad, " +
                    "c.nombre as curso_nombre, c.codigo as curso_codigo, c.nivel as curso_nivel, c.grado as curso_grado " +
                    "FROM horario h " +
                    "JOIN docente d ON h.docente_id = d.id " +
                    "JOIN aula a ON h.aula_id = a.id " +
                    "JOIN curso c ON h.curso_id = c.id " +
                    "WHERE h.id = ?";
        
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Horario horario = mapResultSetToHorario(rs);
                logger.debug("Horario encontrado con ID: {}", id);
                return Optional.of(horario);
            }
            
            return Optional.empty();
            
        } catch (SQLException e) {
            logger.error("Error al buscar horario por ID: {}", e.getMessage());
            return Optional.empty();
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public List<Horario> listarTodos() {
        String sql = "SELECT h.*, " +
                    "d.dni as docente_dni, d.nombre as docente_nombre, " +
                    "d.apellido_paterno as docente_ap_paterno, d.apellido_materno as docente_ap_materno, " +
                    "a.codigo as aula_codigo, a.nombre as aula_nombre, a.capacidad as aula_capacidad, " +
                    "c.nombre as curso_nombre, c.codigo as curso_codigo, c.nivel as curso_nivel, c.grado as curso_grado " +
                    "FROM horario h " +
                    "JOIN docente d ON h.docente_id = d.id " +
                    "JOIN aula a ON h.aula_id = a.id " +
                    "JOIN curso c ON h.curso_id = c.id " +
                    "ORDER BY h.dia, h.hora_inicio";
        
        List<Horario> horarios = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                horarios.add(mapResultSetToHorario(rs));
            }
            
            logger.debug("Se encontraron {} horarios", horarios.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar horarios: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return horarios;
    }
    
    @Override
    public List<Horario> listarPorDocente(Integer docenteId) {
        String sql = "SELECT h.*, " +
                    "d.dni as docente_dni, d.nombre as docente_nombre, " +
                    "d.apellido_paterno as docente_ap_paterno, d.apellido_materno as docente_ap_materno, " +
                    "a.codigo as aula_codigo, a.nombre as aula_nombre, a.capacidad as aula_capacidad, " +
                    "c.nombre as curso_nombre, c.codigo as curso_codigo, c.nivel as curso_nivel, c.grado as curso_grado " +
                    "FROM horario h " +
                    "JOIN docente d ON h.docente_id = d.id " +
                    "JOIN aula a ON h.aula_id = a.id " +
                    "JOIN curso c ON h.curso_id = c.id " +
                    "WHERE h.docente_id = ? " +
                    "ORDER BY h.dia, h.hora_inicio";
        
        List<Horario> horarios = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, docenteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                horarios.add(mapResultSetToHorario(rs));
            }
            
            logger.debug("Se encontraron {} horarios para docente {}", horarios.size(), docenteId);
            
        } catch (SQLException e) {
            logger.error("Error al listar horarios por docente: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return horarios;
    }
    
    @Override
    public List<Horario> listarPorAula(Integer aulaId) {
        String sql = "SELECT h.*, " +
                    "d.dni as docente_dni, d.nombre as docente_nombre, " +
                    "d.apellido_paterno as docente_ap_paterno, d.apellido_materno as docente_ap_materno, " +
                    "a.codigo as aula_codigo, a.nombre as aula_nombre, a.capacidad as aula_capacidad, " +
                    "c.nombre as curso_nombre, c.codigo as curso_codigo, c.nivel as curso_nivel, c.grado as curso_grado " +
                    "FROM horario h " +
                    "JOIN docente d ON h.docente_id = d.id " +
                    "JOIN aula a ON h.aula_id = a.id " +
                    "JOIN curso c ON h.curso_id = c.id " +
                    "WHERE h.aula_id = ? " +
                    "ORDER BY h.dia, h.hora_inicio";
        
        List<Horario> horarios = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, aulaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                horarios.add(mapResultSetToHorario(rs));
            }
            
            logger.debug("Se encontraron {} horarios para aula {}", horarios.size(), aulaId);
            
        } catch (SQLException e) {
            logger.error("Error al listar horarios por aula: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return horarios;
    }
    
    @Override
    public List<Horario> listarPorCurso(Integer cursoId) {
        String sql = "SELECT h.*, " +
                    "d.dni as docente_dni, d.nombre as docente_nombre, " +
                    "d.apellido_paterno as docente_ap_paterno, d.apellido_materno as docente_ap_materno, " +
                    "a.codigo as aula_codigo, a.nombre as aula_nombre, a.capacidad as aula_capacidad, " +
                    "c.nombre as curso_nombre, c.codigo as curso_codigo, c.nivel as curso_nivel, c.grado as curso_grado " +
                    "FROM horario h " +
                    "JOIN docente d ON h.docente_id = d.id " +
                    "JOIN aula a ON h.aula_id = a.id " +
                    "JOIN curso c ON h.curso_id = c.id " +
                    "WHERE h.curso_id = ? " +
                    "ORDER BY h.dia, h.hora_inicio";
        
        List<Horario> horarios = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cursoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                horarios.add(mapResultSetToHorario(rs));
            }
            
            logger.debug("Se encontraron {} horarios para curso {}", horarios.size(), cursoId);
            
        } catch (SQLException e) {
            logger.error("Error al listar horarios por curso: {}", e.getMessage());
        } finally {
            dbConnection.closeConnection(conn);
        }
        
        return horarios;
    }
    
    @Override
    public boolean insertar(Horario horario) {
        String sql = "INSERT INTO horario (docente_id, aula_id, curso_id, dia, hora_inicio, hora_fin) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, horario.getDocenteId());
            stmt.setInt(2, horario.getAulaId());
            stmt.setInt(3, horario.getCursoId());
            stmt.setString(4, horario.getDia().name());
            stmt.setTime(5, Time.valueOf(horario.getHoraInicio()));
            stmt.setTime(6, Time.valueOf(horario.getHoraFin()));
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    horario.setId(rs.getInt(1));
                }
                logger.info("Horario insertado: {} - {}", horario.getDia(), horario.getHoraInicio());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al insertar horario: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean actualizar(Horario horario) {
        String sql = "UPDATE horario SET docente_id = ?, aula_id = ?, curso_id = ?, " +
                     "dia = ?, hora_inicio = ?, hora_fin = ? WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, horario.getDocenteId());
            stmt.setInt(2, horario.getAulaId());
            stmt.setInt(3, horario.getCursoId());
            stmt.setString(4, horario.getDia().name());
            stmt.setTime(5, Time.valueOf(horario.getHoraInicio()));
            stmt.setTime(6, Time.valueOf(horario.getHoraFin()));
            stmt.setInt(7, horario.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Horario actualizado: ID {}", horario.getId());
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar horario: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) {
        String sql = "DELETE FROM horario WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Horario eliminado con ID: {}", id);
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar horario: {}", e.getMessage());
            return false;
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean existeChoqueDocente(Integer docenteId, String dia, String horaInicio, String horaFin, Integer horarioIdExcluir) {
        String sql = "SELECT COUNT(*) FROM horario " +
                     "WHERE docente_id = ? AND dia = ? " +
                     "AND ((hora_inicio < ? AND hora_fin > ?) OR " +
                     "     (hora_inicio < ? AND hora_fin > ?) OR " +
                     "     (hora_inicio >= ? AND hora_fin <= ?))";
        
        if (horarioIdExcluir != null) {
            sql += " AND id != ?";
        }
        
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, docenteId);
            stmt.setString(2, dia);
            stmt.setString(3, horaFin);
            stmt.setString(4, horaInicio);
            stmt.setString(5, horaFin);
            stmt.setString(6, horaFin);
            stmt.setString(7, horaInicio);
            stmt.setString(8, horaFin);
            
            if (horarioIdExcluir != null) {
                stmt.setInt(9, horarioIdExcluir);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                boolean hayChoque = rs.getInt(1) > 0;
                if (hayChoque) {
                    logger.warn("Choque detectado para docente {} en {} {}-{}", 
                               docenteId, dia, horaInicio, horaFin);
                }
                return hayChoque;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al verificar choque de docente: {}", e.getMessage());
            return true; // Por seguridad, asumir que hay choque
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    @Override
    public boolean existeChoqueAula(Integer aulaId, String dia, String horaInicio, String horaFin, Integer horarioIdExcluir) {
        String sql = "SELECT COUNT(*) FROM horario " +
                     "WHERE aula_id = ? AND dia = ? " +
                     "AND ((hora_inicio < ? AND hora_fin > ?) OR " +
                     "     (hora_inicio < ? AND hora_fin > ?) OR " +
                     "     (hora_inicio >= ? AND hora_fin <= ?))";
        
        if (horarioIdExcluir != null) {
            sql += " AND id != ?";
        }
        
        Connection conn = null;
        
        try {
            conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, aulaId);
            stmt.setString(2, dia);
            stmt.setString(3, horaFin);
            stmt.setString(4, horaInicio);
            stmt.setString(5, horaFin);
            stmt.setString(6, horaFin);
            stmt.setString(7, horaInicio);
            stmt.setString(8, horaFin);
            
            if (horarioIdExcluir != null) {
                stmt.setInt(9, horarioIdExcluir);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                boolean hayChoque = rs.getInt(1) > 0;
                if (hayChoque) {
                    logger.warn("Choque detectado para aula {} en {} {}-{}", 
                               aulaId, dia, horaInicio, horaFin);
                }
                return hayChoque;
            }
            
            return false;
            
        } catch (SQLException e) {
            logger.error("Error al verificar choque de aula: {}", e.getMessage());
            return true; // Por seguridad, asumir que hay choque
        } finally {
            dbConnection.closeConnection(conn);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Horario completo con sus relaciones
     */
    private Horario mapResultSetToHorario(ResultSet rs) throws SQLException {
        Horario horario = new Horario();
        
        // Datos del horario
        horario.setId(rs.getInt("id"));
        horario.setDocenteId(rs.getInt("docente_id"));
        horario.setAulaId(rs.getInt("aula_id"));
        horario.setCursoId(rs.getInt("curso_id"));
       String diaStr = rs.getString("dia");
        horario.setDia(Horario.Dia.valueOf(diaStr));
        horario.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
        horario.setHoraFin(rs.getTime("hora_fin").toLocalTime());
        
        // Crear objetos relacionados (para mostrar en vistas)
        Docente docente = new Docente();
        docente.setId(rs.getInt("docente_id"));
        docente.setDni(rs.getString("docente_dni"));
        docente.setNombre(rs.getString("docente_nombre"));
        docente.setApellidoPaterno(rs.getString("docente_ap_paterno"));
        docente.setApellidoMaterno(rs.getString("docente_ap_materno"));
        horario.setDocente(docente);
        
        Aula aula = new Aula();
        aula.setId(rs.getInt("aula_id"));
        aula.setCodigo(rs.getString("aula_codigo"));
        aula.setNombre(rs.getString("aula_nombre"));
        aula.setCapacidad(rs.getInt("aula_capacidad"));
        horario.setAula(aula);
        
        Curso curso = new Curso();
        curso.setId(rs.getInt("curso_id"));
        curso.setNombre(rs.getString("curso_nombre"));
        curso.setCodigo(rs.getString("curso_codigo"));
        String nivelStr = rs.getString("curso_nivel");
        curso.setNivel(Curso.Nivel.valueOf(nivelStr));
        curso.setGrado(rs.getInt("curso_grado"));
        horario.setCurso(curso);
        
        return horario;
    }
}