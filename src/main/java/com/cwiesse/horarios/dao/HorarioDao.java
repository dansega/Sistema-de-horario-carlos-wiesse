package com.cwiesse.horarios.dao;

import com.cwiesse.horarios.model.Horario;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones con la tabla horario.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public interface HorarioDao {
    
    /**
     * Busca un horario por su ID
     */
    Optional<Horario> buscarPorId(Integer id);
    
    /**
     * Lista todos los horarios con información completa (JOINs)
     */
    List<Horario> listarTodos();
    
    /**
     * Lista horarios de un docente específico
     */
    List<Horario> listarPorDocente(Integer docenteId);
    
    /**
     * Lista horarios de un aula específica
     */
    List<Horario> listarPorAula(Integer aulaId);
    
    /**
     * Lista horarios de un curso específico
     */
    List<Horario> listarPorCurso(Integer cursoId);
    
    /**
     * Inserta un nuevo horario
     */
    boolean insertar(Horario horario);
    
    /**
     * Actualiza un horario existente
     */
    boolean actualizar(Horario horario);
    
    /**
     * Elimina un horario por su ID
     */
    boolean eliminar(Integer id);
    
    /**
     * Verifica si hay choque de horarios para un docente
     * @return true si hay choque, false si no hay choque
     */
    boolean existeChoqueDocente(Integer docenteId, String dia, String horaInicio, String horaFin, Integer horarioIdExcluir);
    
    /**
     * Verifica si hay choque de horarios para un aula
     * @return true si hay choque, false si no hay choque
     */
    boolean existeChoqueAula(Integer aulaId, String dia, String horaInicio, String horaFin, Integer horarioIdExcluir);
}