package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.UsuarioDao;
import com.cwiesse.horarios.dao.impl.UsuarioDaoImpl;
import com.cwiesse.horarios.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Servlet para manejar autenticación (login y logout).
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
@WebServlet(name = "AuthController", urlPatterns = {"/auth", "/login", "/logout"})
public class AuthController extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private UsuarioDao usuarioDao;
    
    @Override
    public void init() throws ServletException {
        usuarioDao = new UsuarioDaoImpl();
        logger.info("AuthController inicializado");
    }
    
    /**
     * Maneja GET - Muestra formulario de login o procesa logout
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        if ("/logout".equals(path)) {
            procesarLogout(request, response);
        } else {
            mostrarLogin(request, response);
        }
    }
    
    /**
     * Maneja POST - Procesa el login
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        if ("/login".equals(path) || "/auth".equals(path)) {
            procesarLogin(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    /**
     * Muestra el formulario de login
     */
    private void mostrarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Si ya está logueado, redirigir al dashboard según rol
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            
            if (usuario.getRol() == Usuario.Rol.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else if (usuario.getRol() == Usuario.Rol.DOCENTE) {
                response.sendRedirect(request.getContextPath() + "/docente/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
            return;
        }
        
        // Mostrar página de login
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    /**
     * Procesa el login del usuario
     */
    private void procesarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        logger.debug("Intento de login para usuario: {}", username);
        
        // Validar campos
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("error", "Por favor complete todos los campos");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        // Buscar usuario (incluye inactivos)
        Optional<Usuario> usuarioOpt = usuarioDao.buscarPorUsernameIncluirInactivos(username.trim());
        
        if (usuarioOpt.isEmpty()) {
            logger.warn("Usuario no encontrado: {}", username);
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar si el usuario está activo
        if (!usuario.isActivo()) {
            logger.warn("Intento de login con cuenta desactivada: {}", username);
            request.setAttribute("error", "Su cuenta ha sido desactivada. Por favor, contacte al administrador.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        // Verificar contraseña
        if (!BCrypt.checkpw(password, usuario.getPasswordHash())) {
            logger.warn("Contraseña incorrecta para usuario: {}", username);
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        // Login exitoso - Crear sesión
        HttpSession session = request.getSession(true);
        session.setAttribute("usuario", usuario);
        session.setAttribute("username", usuario.getUsername());
        session.setAttribute("rol", usuario.getRol().name());
        session.setMaxInactiveInterval(30 * 60); // 30 minutos
        
        logger.info("Login exitoso: {} ({})", usuario.getUsername(), usuario.getRol());
        
        // Redirigir según el rol
        if (usuario.getRol() == Usuario.Rol.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else if (usuario.getRol() == Usuario.Rol.DOCENTE) {
            response.sendRedirect(request.getContextPath() + "/docente/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    /**
     * Procesa el logout del usuario
     */
    private void procesarLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            String username = (String) session.getAttribute("username");
            session.invalidate();
            logger.info("Logout exitoso: {}", username);
        }
        
        response.sendRedirect(request.getContextPath() + "/login");
    }
}