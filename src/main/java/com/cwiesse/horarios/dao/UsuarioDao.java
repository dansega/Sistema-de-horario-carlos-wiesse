package com.cwiesse.horarios.dao;

import com.cwiesse.horarios.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz DAO para operaciones con la tabla usuario.
 * Define el contrato que debe cumplir la implementación.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public interface UsuarioDao {
    
    /**
     * Busca un usuario por su username
     * @param username Username del usuario
     * @return Optional con el usuario si existe, Optional.empty() si no
     */
    Optional<Usuario> buscarPorUsername(String username);
    
    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Optional con el usuario si existe, Optional.empty() si no
     */
    Optional<Usuario> buscarPorId(Integer id);
    
    /**
     * Lista todos los usuarios
     * @return Lista de usuarios
     */
    List<Usuario> listarTodos();
    
    /**
     * Inserta un nuevo usuario
     * @param usuario Usuario a insertar
     * @return true si se insertó correctamente, false si no
     */
    boolean insertar(Usuario usuario);
    
    /**
     * Actualiza un usuario existente
     * @param usuario Usuario a actualizar
     * @return true si se actualizó correctamente, false si no
     */
    boolean actualizar(Usuario usuario);
    
    /**
     * Elimina un usuario por su ID
     * @param id ID del usuario a eliminar
     * @return true si se eliminó correctamente, false si no
     */
    boolean eliminar(Integer id);
    
    /**
     * Verifica si existe un usuario con ese username
     * @param username Username a verificar
     * @return true si existe, false si no
     */
    boolean existeUsername(String username);
}