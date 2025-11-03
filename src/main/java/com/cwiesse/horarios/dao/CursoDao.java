package com.cwiesse.horarios.dao;

import com.cwiesse.horarios.model.Curso;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones con la tabla curso.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public interface CursoDao {
    
    /**
     * Busca un curso por su ID
     */
    Optional<Curso> buscarPorId(Integer id);
    
    /**
     * Busca un curso por su código
     */
    Optional<Curso> buscarPorCodigo(String codigo);
    
    /**
     * Lista todos los cursos
     */
    List<Curso> listarTodos();
    
    /**
     * Lista solo cursos activos
     */
    List<Curso> listarActivos();
    
    /**
     * Inserta un nuevo curso
     */
    boolean insertar(Curso curso);
    
    /**
     * Actualiza un curso existente
     */
    boolean actualizar(Curso curso);
    
    /**
     * Elimina un curso por su ID
     */
    boolean eliminar(Integer id);
    
    /**
     * Verifica si existe un curso con ese código
     */
    boolean existeCodigo(String codigo);
}