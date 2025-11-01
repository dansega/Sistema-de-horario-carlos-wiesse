package com.cwiesse.horarios.dao;

import com.cwiesse.horarios.model.Docente;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones con la tabla docente.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public interface DocenteDao {
    
    /**
     * Busca un docente por su ID
     */
    Optional<Docente> buscarPorId(Integer id);
    
    /**
     * Busca un docente por su DNI
     */
    Optional<Docente> buscarPorDni(String dni);
    
    /**
     * Lista todos los docentes activos
     */
    List<Docente> listarTodos();
    
    /**
     * Lista solo docentes activos
     */
    List<Docente> listarActivos();
    
    /**
     * Inserta un nuevo docente
     */
    boolean insertar(Docente docente);
    
    /**
     * Actualiza un docente existente
     */
    boolean actualizar(Docente docente);
    
    /**
     * Elimina un docente por su ID
     */
    boolean eliminar(Integer id);
    
    /**
     * Desactiva un docente (borrado lógico)
     */
    boolean desactivar(Integer id);
    
    /**
     * Verifica si existe un docente con ese DNI
     */
    boolean existeDni(String dni);
}