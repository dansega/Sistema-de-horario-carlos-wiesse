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
     * Busca un curso por su nombre
     */
    Optional<Curso> buscarPorNombre(String nombre);
    
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
     * Elimina un curso por su ID (soft delete)
     */
    boolean eliminar(Integer id);
    
    /**
     * Verifica si existe un curso con ese nombre
     */
    boolean existeNombre(String nombre);
    
    /**
     * Verifica si existe otro curso con el mismo nombre (para edición)
     */
    boolean existeNombreExceptoId(String nombre, Integer id);
}