package com.cwiesse.horarios.dao;

import com.cwiesse.horarios.model.Aula;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones con la tabla aula.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public interface AulaDao {
    
    /**
     * Busca un aula por su ID
     */
    Optional<Aula> buscarPorId(Integer id);
    
    /**
     * Busca un aula por su código
     */
    Optional<Aula> buscarPorCodigo(String codigo);
    
    /**
     * Lista todas las aulas
     */
    List<Aula> listarTodas();
    
    /**
     * Lista solo aulas activas
     */
    List<Aula> listarActivas();
    
    /**
     * Inserta una nueva aula
     */
    boolean insertar(Aula aula);
    
    /**
     * Actualiza un aula existente
     */
    boolean actualizar(Aula aula);
    
    /**
     * Elimina un aula por su ID
     */
    boolean eliminar(Integer id);
    
    /**
     * Verifica si existe un aula con ese código
     */
    boolean existeCodigo(String codigo);
}