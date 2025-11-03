package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.AulaDao;
import com.cwiesse.horarios.dao.impl.AulaDaoImpl;
import com.cwiesse.horarios.model.Aula;
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

/**
 * Servlet para manejar el CRUD de aulas.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
@WebServlet(name = "AulaController", urlPatterns = {"/aulas", "/aulas/*"})
public class AulaController extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(AulaController.class);
    private AulaDao aulaDao;
    
    @Override
    public void init() throws ServletException {
        aulaDao = new AulaDaoImpl();
        logger.info("AulaController inicializado");
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
                listarAulas(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            default:
                listarAulas(request, response);
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
                crearAula(request, response);
                break;
            case "actualizar":
                actualizarAula(request, response);
                break;
            case "eliminar":
                eliminarAula(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void listarAulas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Aula> aulas = aulaDao.listarTodas();
        
        request.setAttribute("aulas", aulas);
        request.setAttribute("totalAulas", aulas.size());
        
        logger.debug("Listando {} aulas", aulas.size());
        
        request.getRequestDispatcher("/WEB-INF/views/aulas/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/aulas/formulario.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/aulas?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            Optional<Aula> aulaOpt = aulaDao.buscarPorId(id);
            
            if (aulaOpt.isPresent()) {
                request.setAttribute("aula", aulaOpt.get());
                request.getRequestDispatcher("/WEB-INF/views/aulas/formulario.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Aula no encontrada");
                listarAulas(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/aulas?action=listar");
        }
    }
    
    private void crearAula(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String codigo = request.getParameter("codigo");
        String nombre = request.getParameter("nombre");
        String capacidadStr = request.getParameter("capacidad");
        String pisoStr = request.getParameter("piso");
        String edificio = request.getParameter("edificio");
        
        // Validar campos obligatorios
        if (codigo == null || codigo.trim().isEmpty() ||
            capacidadStr == null || capacidadStr.trim().isEmpty() ||
            pisoStr == null || pisoStr.trim().isEmpty()) {
            
            request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
            request.setAttribute("codigo", codigo);
            request.setAttribute("nombre", nombre);
            request.setAttribute("capacidad", capacidadStr);
            request.setAttribute("piso", pisoStr);
            request.setAttribute("edificio", edificio);
            
            request.getRequestDispatcher("/WEB-INF/views/aulas/formulario.jsp").forward(request, response);
            return;
        }
        
        try {
            int capacidad = Integer.parseInt(capacidadStr);
            int piso = Integer.parseInt(pisoStr);
            
            // Validar rango de capacidad
            if (capacidad < 1 || capacidad > 50) {
                request.setAttribute("error", "La capacidad debe estar entre 1 y 50");
                request.getRequestDispatcher("/WEB-INF/views/aulas/formulario.jsp").forward(request, response);
                return;
            }
            
            // Validar rango de piso
            if (piso < 1 || piso > 5) {
                request.setAttribute("error", "El piso debe estar entre 1 y 5");
                request.getRequestDispatcher("/WEB-INF/views/aulas/formulario.jsp").forward(request, response);
                return;
            }
            
            // Verificar si el código ya existe
            if (aulaDao.existeCodigo(codigo)) {
                request.setAttribute("error", "Ya existe un aula con ese código");
                request.getRequestDispatcher("/WEB-INF/views/aulas/formulario.jsp").forward(request, response);
                return;
            }
            
            // Crear aula
            Aula aula = new Aula();
            aula.setCodigo(codigo.trim().toUpperCase());
            aula.setNombre(nombre != null ? nombre.trim() : null);
            aula.setCapacidad(capacidad);
            aula.setPiso(piso);
            aula.setEdificio(edificio != null ? edificio.trim().toUpperCase() : null);
            aula.setEstado(true);
            
            boolean creada = aulaDao.insertar(aula);
            
            if (creada) {
                logger.info("Aula creada: {}", aula.getCodigo());
                request.getSession().setAttribute("mensaje", "Aula creada exitosamente");
                response.sendRedirect(request.getContextPath() + "/aulas?action=listar");
            } else {
                request.setAttribute("error", "Error al crear el aula");
                request.getRequestDispatcher("/WEB-INF/views/aulas/formulario.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Capacidad y piso deben ser números válidos");
            request.getRequestDispatcher("/WEB-INF/views/aulas/formulario.jsp").forward(request, response);
        }
    }
    
    private void actualizarAula(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/aulas?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            String codigo = request.getParameter("codigo");
            String nombre = request.getParameter("nombre");
            int capacidad = Integer.parseInt(request.getParameter("capacidad"));
            int piso = Integer.parseInt(request.getParameter("piso"));
            String edificio = request.getParameter("edificio");
            
            Optional<Aula> aulaOpt = aulaDao.buscarPorId(id);
            
            if (aulaOpt.isEmpty()) {
                request.setAttribute("error", "Aula no encontrada");
                listarAulas(request, response);
                return;
            }
            
            Aula aula = aulaOpt.get();
            aula.setCodigo(codigo.trim().toUpperCase());
            aula.setNombre(nombre != null ? nombre.trim() : null);
            aula.setCapacidad(capacidad);
            aula.setPiso(piso);
            aula.setEdificio(edificio != null ? edificio.trim().toUpperCase() : null);
            
            boolean actualizada = aulaDao.actualizar(aula);
            
            if (actualizada) {
                logger.info("Aula actualizada: {}", aula.getCodigo());
                request.getSession().setAttribute("mensaje", "Aula actualizada exitosamente");
                response.sendRedirect(request.getContextPath() + "/aulas?action=listar");
            } else {
                request.setAttribute("error", "Error al actualizar el aula");
                request.setAttribute("aula", aula);
                request.getRequestDispatcher("/WEB-INF/views/aulas/formulario.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/aulas?action=listar");
        }
    }
    
    private void eliminarAula(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/aulas?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            boolean eliminada = aulaDao.eliminar(id);
            
            if (eliminada) {
                logger.info("Aula eliminada con ID: {}", id);
                request.getSession().setAttribute("mensaje", "Aula eliminada exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el aula");
            }
            
            response.sendRedirect(request.getContextPath() + "/aulas?action=listar");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/aulas?action=listar");
        }
    }
}