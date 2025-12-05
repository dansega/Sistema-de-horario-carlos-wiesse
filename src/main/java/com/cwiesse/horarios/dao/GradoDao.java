package com.cwiesse.horarios.dao;

import com.cwiesse.horarios.model.Grado;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones CRUD de Grado.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public interface GradoDao {
    
    /**
     * Busca un grado por su ID
     * 
     * @param id ID del grado
     * @return Optional con el grado si existe
     */
    Optional<Grado> buscarPorId(Integer id);
    
    /**
     * Lista todos los grados (activos e inactivos)
     * 
     * @return Lista de todos los grados
     */
    List<Grado> listarTodos();
    
    /**
     * Lista solo los grados activos
     * 
     * @return Lista de grados activos
     */
    List<Grado> listarActivos();
    
    /**
     * Lista grados por nivel educativo
     * 
     * @param nivel Nivel educativo (PRIMARIA o SECUNDARIA)
     * @return Lista de grados del nivel especificado
     */
    List<Grado> listarPorNivel(Grado.Nivel nivel);
    
    /**
     * Inserta un nuevo grado
     * 
     * @param grado Grado a insertar
     * @return true si se insertó correctamente
     */
    boolean insertar(Grado grado);
    
    /**
     * Actualiza un grado existente
     * 
     * @param grado Grado con datos actualizados
     * @return true si se actualizó correctamente
     */
    boolean actualizar(Grado grado);
    
    /**
     * Elimina un grado (soft delete - cambia estado a false)
     * 
     * @param id ID del grado a eliminar
     * @return true si se eliminó correctamente
     */
    boolean eliminar(Integer id);
    
    /**
     * Verifica si existe un grado con la combinación nivel-número-sección
     * 
     * @param nivel Nivel educativo
     * @param numero Número de grado
     * @param seccion Sección
     * @return true si ya existe
     */
    boolean existeGrado(Grado.Nivel nivel, Integer numero, String seccion);
    
    /**
     * Verifica si existe un grado con la combinación nivel-número-sección
     * excluyendo un ID específico (útil para edición)
     * 
     * @param nivel Nivel educativo
     * @param numero Número de grado
     * @param seccion Sección
     * @param idExcluir ID a excluir de la búsqueda
     * @return true si ya existe otro grado con esa combinación
     */
    boolean existeGradoExceptoId(Grado.Nivel nivel, Integer numero, String seccion, Integer idExcluir);
}