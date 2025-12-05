package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.DocenteDao;
import com.cwiesse.horarios.dao.HorarioDao;
import com.cwiesse.horarios.dao.impl.DocenteDaoImpl;
import com.cwiesse.horarios.dao.impl.HorarioDaoImpl;
import com.cwiesse.horarios.model.Docente;
import com.cwiesse.horarios.model.Horario;
import com.cwiesse.horarios.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Servlet para el dashboard del docente.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
@WebServlet(name = "DocenteDashboardController", urlPatterns = {"/docente/dashboard"})
public class DocenteDashboardController extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(DocenteDashboardController.class);
    private DocenteDao docenteDao;
    private HorarioDao horarioDao;
    
    @Override
    public void init() throws ServletException {
        docenteDao = new DocenteDaoImpl();
        horarioDao = new HorarioDaoImpl();
        logger.info("DocenteDashboardController inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar que hay sesión
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        // Verificar que es DOCENTE
        if (usuario.getRol() != Usuario.Rol.DOCENTE) {
            logger.warn("Usuario {} con rol {} intentó acceder al dashboard de docente", 
                       usuario.getUsername(), usuario.getRol());
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        try {
            // Buscar el docente asociado a este usuario
            Optional<Docente> docenteOpt = docenteDao.buscarPorUsuarioId(usuario.getId());
            
            if (docenteOpt.isEmpty()) {
                logger.error("No se encontró docente asociado al usuario: {}", usuario.getUsername());
                request.setAttribute("error", "No se encontró información del docente. Por favor contacte al administrador.");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }
            
            Docente docente = docenteOpt.get();
            logger.debug("Docente encontrado: {} {}", docente.getNombre(), docente.getApellidoPaterno());
            
            // Obtener los horarios del docente
            List<Horario> horarios = horarioDao.listarPorDocente(docente.getId());
            logger.debug("Se encontraron {} horarios para el docente", horarios.size());
            
            // Obtener los cursos que dicta el docente
            List<String> cursos = docenteDao.obtenerCursosDelDocente(docente.getId());
            docente.setCursosQueDicta(cursos);
            logger.debug("Docente dicta {} cursos diferentes", cursos.size());
            
            // Pasar datos a la vista
            request.setAttribute("docente", docente);
            request.setAttribute("horarios", horarios);
            request.setAttribute("totalHorarios", horarios.size());
            
            // Mostrar dashboard del docente
            request.getRequestDispatcher("/WEB-INF/views/docente-dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error al cargar dashboard del docente: {}", e.getMessage(), e);
            request.setAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
