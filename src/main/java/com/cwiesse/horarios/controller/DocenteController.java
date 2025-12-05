package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.DocenteDao;
import com.cwiesse.horarios.dao.UsuarioDao;
import com.cwiesse.horarios.dao.impl.DocenteDaoImpl;
import com.cwiesse.horarios.dao.impl.UsuarioDaoImpl;
import com.cwiesse.horarios.model.Docente;
import com.cwiesse.horarios.model.Usuario;
import com.cwiesse.horarios.util.Validation;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "DocenteController", urlPatterns = {"/docentes", "/docentes/*"})
public class DocenteController extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(DocenteController.class);
    private DocenteDao docenteDao;
    private UsuarioDao usuarioDao;
    
    @Override
    public void init() throws ServletException {
        docenteDao = new DocenteDaoImpl();
        usuarioDao = new UsuarioDaoImpl();
        logger.info("DocenteController inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "listar";
        }
        
        switch (action) {
            case "listar":
                listarDocentes(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "ver":
                verDetalle(request, response);
                break;
            default:
                listarDocentes(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        switch (action) {
            case "crear":
                crearDocente(request, response);
                break;
            case "actualizar":
                actualizarDocente(request, response);
                break;
            case "eliminar":
                eliminarDocente(request, response);
                break;
            case "desactivarUsuario":
                desactivarUsuarioDocente(request, response);
                break;
            case "activarUsuario":
                activarUsuarioDocente(request, response);
                break;
            case "resetearPassword":
                resetearPasswordDocente(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void listarDocentes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Docente> docentes = docenteDao.listarTodos();
        
        // Cargar los cursos de cada docente
        for (Docente docente : docentes) {
            List<String> cursos = docenteDao.obtenerCursosDelDocente(docente.getId());
            docente.setCursosQueDicta(cursos);
        }
        
        request.setAttribute("docentes", docentes);
        request.setAttribute("totalDocentes", docentes.size());
        
        logger.debug("Listando {} docentes con sus cursos", docentes.size());
        
        request.getRequestDispatcher("/WEB-INF/views/docentes/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            Optional<Docente> docenteOpt = docenteDao.buscarPorId(id);
            
            if (docenteOpt.isPresent()) {
                Docente docente = docenteOpt.get();
                
                // ✅ NUEVO: Si tiene usuarioId, cargar el usuario completo con su rol
                if (docente.getUsuarioId() != null) {
                    Optional<Usuario> usuarioOpt = usuarioDao.buscarPorId(docente.getUsuarioId());
                    if (usuarioOpt.isPresent()) {
                        docente.setUsuario(usuarioOpt.get());
                        logger.debug("Usuario cargado para docente {}: {} (Rol: {})", 
                            id, usuarioOpt.get().getUsername(), usuarioOpt.get().getRol());
                    }
                }
                
                request.setAttribute("docente", docente);
                request.setAttribute("tieneUsuario", docenteDao.tieneUsuario(id));
                request.setAttribute("usuarioActivo", docenteDao.usuarioActivo(id));
                request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Docente no encontrado");
                listarDocentes(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        }
    }
    
    private void verDetalle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            Optional<Docente> docenteOpt = docenteDao.buscarPorId(id);
            
            if (docenteOpt.isPresent()) {
                request.setAttribute("docente", docenteOpt.get());
                request.getRequestDispatcher("/WEB-INF/views/docentes/detalle.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Docente no encontrado");
                listarDocentes(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        }
    }
    
    private void crearDocente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String dni = request.getParameter("dni");
        String nombre = request.getParameter("nombre");
        String apellidoPaterno = request.getParameter("apellidoPaterno");
        String apellidoMaterno = request.getParameter("apellidoMaterno");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String crearUsuarioParam = request.getParameter("crearUsuario");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        boolean crearUsuario = "on".equals(crearUsuarioParam) || "true".equals(crearUsuarioParam);
        
        if (!Validation.isNotEmpty(dni) || !Validation.isNotEmpty(nombre) ||
            !Validation.isNotEmpty(apellidoPaterno) || !Validation.isNotEmpty(apellidoMaterno)) {
            
            request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
            request.setAttribute("dni", dni);
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellidoPaterno", apellidoPaterno);
            request.setAttribute("apellidoMaterno", apellidoMaterno);
            request.setAttribute("email", email);
            request.setAttribute("telefono", telefono);
            
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            return;
        }
        
        if (!Validation.isValidDNI(dni)) {
            request.setAttribute("error", "DNI inválido. Debe tener 8 dígitos");
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            return;
        }
        
        if (Validation.isNotEmpty(email) && !Validation.isValidEmail(email)) {
            request.setAttribute("error", "Email inválido");
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            return;
        }
        
        if (docenteDao.existeDni(dni)) {
            request.setAttribute("error", "Ya existe un docente con ese DNI");
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            return;
        }
        
        Integer usuarioId = null;
        if (crearUsuario) {
            if (!Validation.isNotEmpty(username) || !Validation.isNotEmpty(password)) {
                request.setAttribute("error", "Debe ingresar usuario y contraseña");
                request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
                return;
            }
            
            if (usuarioDao.existeUsername(username.trim())) {
                request.setAttribute("error", "El nombre de usuario ya existe");
                request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
                return;
            }
            
            // Obtener y validar rol
            String rolParam = request.getParameter("rol");
            if (rolParam == null || rolParam.trim().isEmpty()) {
                request.setAttribute("error", "Debe seleccionar un rol para el usuario");
                request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
                return;
            }
            
            Usuario.Rol rol;
            try {
                rol = Usuario.Rol.valueOf(rolParam);
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Rol inválido");
                request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
                return;
            }
            
            try {
                Usuario usuario = new Usuario();
                usuario.setUsername(username.trim());
                usuario.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
                usuario.setRol(rol);
                usuario.setActivo(true);
                
                if (usuarioDao.insertar(usuario)) {
                    usuarioId = usuario.getId();
                    logger.info("Usuario creado: {} con rol {}", username, rol);
                } else {
                    request.setAttribute("error", "Error al crear el usuario de acceso");
                    request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
                    return;
                }
            } catch (Exception e) {
                logger.error("Error al crear usuario: {}", e.getMessage());
                request.setAttribute("error", "Error al crear el usuario de acceso");
                request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
                return;
            }
        }
        
        Docente docente = new Docente(dni, nombre, apellidoPaterno, apellidoMaterno);
        docente.setEmail(email);
        docente.setTelefono(telefono);
        docente.setUsuarioId(usuarioId);
        
        boolean creado = docenteDao.insertar(docente);
        
        if (creado) {
            logger.info("Docente creado: {}", docente.getNombreCompleto());
            request.getSession().setAttribute("mensaje", 
                crearUsuario ? 
                "Docente creado exitosamente con acceso al sistema (Usuario: " + username + ")" :
                "Docente creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        } else {
            request.setAttribute("error", "Error al crear el docente");
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
        }
    }
    
    private void actualizarDocente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            String apellidoPaterno = request.getParameter("apellidoPaterno");
            String apellidoMaterno = request.getParameter("apellidoMaterno");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String crearUsuarioParam = request.getParameter("crearUsuario");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String nuevoRolParam = request.getParameter("nuevoRol");
            
            boolean crearUsuario = "on".equals(crearUsuarioParam);
            
            Optional<Docente> docenteOpt = docenteDao.buscarPorId(id);
            
            if (docenteOpt.isEmpty()) {
                request.setAttribute("error", "Docente no encontrado");
                listarDocentes(request, response);
                return;
            }
            
            Docente docente = docenteOpt.get();
            
            // ✅ Si el docente YA tiene usuario y se especificó un nuevo rol
            if (docente.getUsuarioId() != null && nuevoRolParam != null && !nuevoRolParam.trim().isEmpty()) {
                try {
                    Usuario.Rol nuevoRol = Usuario.Rol.valueOf(nuevoRolParam);
                    Optional<Usuario> usuarioOpt = usuarioDao.buscarPorId(docente.getUsuarioId());
                    
                    if (usuarioOpt.isPresent()) {
                        Usuario usuario = usuarioOpt.get();
                        Usuario.Rol rolAnterior = usuario.getRol();
                        usuario.setRol(nuevoRol);
                        
                        if (usuarioDao.actualizar(usuario)) {
                            logger.info("Rol actualizado para usuario {} de {} a {}", 
                                usuario.getUsername(), rolAnterior, nuevoRol);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    logger.error("Rol inválido especificado: {}", nuevoRolParam);
                }
            }
            
            // Crear usuario si no tiene y se marcó checkbox
            if (crearUsuario && docente.getUsuarioId() == null) {
                if (username != null && !username.trim().isEmpty() && 
                    password != null && !password.trim().isEmpty()) {
                    
                    if (!usuarioDao.existeUsername(username.trim())) {
                        // Obtener rol
                        String rolParam = request.getParameter("rol");
                        Usuario.Rol rol = Usuario.Rol.DOCENTE; // Default
                        
                        if (rolParam != null && !rolParam.trim().isEmpty()) {
                            try {
                                rol = Usuario.Rol.valueOf(rolParam);
                            } catch (IllegalArgumentException e) {
                                rol = Usuario.Rol.DOCENTE;
                            }
                        }
                        
                        Usuario usuario = new Usuario();
                        usuario.setUsername(username.trim());
                        usuario.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
                        usuario.setRol(rol);
                        usuario.setActivo(true);
                        
                        if (usuarioDao.insertar(usuario)) {
                            docente.setUsuarioId(usuario.getId());
                            logger.info("Usuario creado para docente existente: {} con rol {}", username, rol);
                        }
                    }
                }
            }
            
            docente.setDni(dni);
            docente.setNombre(nombre);
            docente.setApellidoPaterno(apellidoPaterno);
            docente.setApellidoMaterno(apellidoMaterno);
            docente.setEmail(email);
            docente.setTelefono(telefono);
            
            if (docenteDao.actualizar(docente)) {
                String mensajeExito = "Docente actualizado exitosamente";
                
                // Agregar mensaje si se cambió el rol
                if (nuevoRolParam != null && !nuevoRolParam.trim().isEmpty()) {
                    mensajeExito += " (Rol cambiado a " + nuevoRolParam + ")";
                }
                
                request.getSession().setAttribute("mensaje", mensajeExito);
                response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            } else {
                request.setAttribute("error", "Error al actualizar el docente en la base de datos");
                request.setAttribute("docente", docente);
                request.setAttribute("tieneUsuario", docenteDao.tieneUsuario(id));
                request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error al actualizar docente", e);
            request.setAttribute("error", "ERROR: " + e.getMessage());
            try {
                Optional<Docente> docenteOpt = docenteDao.buscarPorId(Integer.parseInt(idStr));
                if (docenteOpt.isPresent()) {
                    Docente docente = docenteOpt.get();
                    
                    // ✅ Cargar usuario si existe
                    if (docente.getUsuarioId() != null) {
                        Optional<Usuario> usuarioOpt = usuarioDao.buscarPorId(docente.getUsuarioId());
                        usuarioOpt.ifPresent(docente::setUsuario);
                    }
                    
                    request.setAttribute("docente", docente);
                    request.setAttribute("tieneUsuario", docenteDao.tieneUsuario(Integer.parseInt(idStr)));
                }
            } catch (Exception ex) {
                logger.error("Error al recuperar docente después de fallo", ex);
            }
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
        }
    }
    
    private void eliminarDocente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            // Buscar el docente para obtener su usuario_id
            Optional<Docente> docenteOpt = docenteDao.buscarPorId(id);
            
            if (docenteOpt.isEmpty()) {
                request.getSession().setAttribute("error", "Docente no encontrado");
                response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
                return;
            }
            
            Docente docente = docenteOpt.get();
            Integer usuarioId = docente.getUsuarioId();
            
            // Primero eliminar el docente
            boolean docenteEliminado = docenteDao.eliminar(id);
            
            if (docenteEliminado) {
                logger.info("Docente eliminado con ID: {}", id);
                
                // Si tenía usuario asociado, eliminarlo también
                if (usuarioId != null) {
                    boolean usuarioEliminado = usuarioDao.eliminar(usuarioId);
                    if (usuarioEliminado) {
                        logger.info("Usuario eliminado completamente con ID: {}", usuarioId);
                        request.getSession().setAttribute("mensaje", "Docente y su usuario eliminados completamente. El usuario puede ser reutilizado.");
                    } else {
                        logger.warn("No se pudo eliminar el usuario con ID: {}", usuarioId);
                        request.getSession().setAttribute("mensaje", "Docente eliminado, pero hubo un problema al eliminar su usuario");
                    }
                } else {
                    request.getSession().setAttribute("mensaje", "Docente eliminado exitosamente");
                }
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el docente");
            }
            
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        }
    }
    
    private void desactivarUsuarioDocente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            return;
        }
        
        try {
            Integer docenteId = Integer.parseInt(idStr);
            
            Optional<Docente> docenteOpt = docenteDao.buscarPorId(docenteId);
            
            if (docenteOpt.isEmpty()) {
                request.getSession().setAttribute("error", "Docente no encontrado");
                response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
                return;
            }
            
            Docente docente = docenteOpt.get();
            
            if (docente.getUsuarioId() == null) {
                request.getSession().setAttribute("error", "Este docente no tiene usuario asignado");
                response.sendRedirect(request.getContextPath() + "/docentes?action=editar&id=" + docenteId);
                return;
            }
            
            if (usuarioDao.desactivar(docente.getUsuarioId())) {
                logger.info("Usuario desactivado para docente: {}", docente.getNombreCompleto());
                request.getSession().setAttribute("mensaje", "Acceso al sistema desactivado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al desactivar el acceso");
            }
            
            response.sendRedirect(request.getContextPath() + "/docentes?action=editar&id=" + docenteId);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        }
    }
    
    private void activarUsuarioDocente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            return;
        }
        
        try {
            Integer docenteId = Integer.parseInt(idStr);
            
            Optional<Docente> docenteOpt = docenteDao.buscarPorId(docenteId);
            
            if (docenteOpt.isEmpty()) {
                request.getSession().setAttribute("error", "Docente no encontrado");
                response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
                return;
            }
            
            Docente docente = docenteOpt.get();
            
            if (docente.getUsuarioId() == null) {
                request.getSession().setAttribute("error", "Este docente no tiene usuario asignado");
                response.sendRedirect(request.getContextPath() + "/docentes?action=editar&id=" + docenteId);
                return;
            }
            
            if (usuarioDao.activar(docente.getUsuarioId())) {
                logger.info("Usuario activado para docente: {}", docente.getNombreCompleto());
                request.getSession().setAttribute("mensaje", "Acceso al sistema activado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al activar el acceso");
            }
            
            response.sendRedirect(request.getContextPath() + "/docentes?action=editar&id=" + docenteId);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        }
    }
    
    private void resetearPasswordDocente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        String nuevaPassword = request.getParameter("nuevaPassword");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            return;
        }
        
        try {
            Integer docenteId = Integer.parseInt(idStr);
            
            Optional<Docente> docenteOpt = docenteDao.buscarPorId(docenteId);
            
            if (docenteOpt.isEmpty()) {
                request.getSession().setAttribute("error", "Docente no encontrado");
                response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
                return;
            }
            
            Docente docente = docenteOpt.get();
            
            if (docente.getUsuarioId() == null) {
                request.getSession().setAttribute("error", "Este docente no tiene usuario asignado");
                response.sendRedirect(request.getContextPath() + "/docentes?action=editar&id=" + docenteId);
                return;
            }
            
            if (nuevaPassword == null || nuevaPassword.trim().isEmpty() || nuevaPassword.length() < 6) {
                request.getSession().setAttribute("error", "La contraseña debe tener al menos 6 caracteres");
                response.sendRedirect(request.getContextPath() + "/docentes?action=editar&id=" + docenteId);
                return;
            }
            
            String nuevoHash = BCrypt.hashpw(nuevaPassword, BCrypt.gensalt());
            
            if (usuarioDao.actualizarPassword(docente.getUsuarioId(), nuevoHash)) {
                logger.info("Contraseña reseteada para docente: {}", docente.getNombreCompleto());
                request.getSession().setAttribute("mensaje", "Contraseña actualizada exitosamente. Nueva contraseña: " + nuevaPassword);
            } else {
                request.getSession().setAttribute("error", "Error al actualizar la contraseña");
            }
            
            response.sendRedirect(request.getContextPath() + "/docentes?action=editar&id=" + docenteId);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        }
    }
}