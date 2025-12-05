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
     * Busca un docente por su usuario_id
     */
    Optional<Docente> buscarPorUsuarioId(Integer usuarioId);
    
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
    
    /**
     * Verifica si existe un docente con acceso al sistema (usuario_id no nulo)
     */
    boolean tieneUsuario(Integer docenteId);
    
    /**
     * Verifica si el usuario del docente está activo
     */
    boolean usuarioActivo(Integer docenteId);
    
    /**
     * Obtiene la lista de cursos únicos que dicta un docente
     * 
     * @param docenteId ID del docente
     * @return Lista de nombres de cursos
     */
    List<String> obtenerCursosDelDocente(Integer docenteId);
}