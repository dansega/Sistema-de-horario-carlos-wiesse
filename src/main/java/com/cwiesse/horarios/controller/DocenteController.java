package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.DocenteDao;
import com.cwiesse.horarios.dao.impl.DocenteDaoImpl;
import com.cwiesse.horarios.model.Docente;
import com.cwiesse.horarios.util.Validation;
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
 * Servlet para manejar el CRUD de docentes.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
@WebServlet(name = "DocenteController", urlPatterns = {"/docentes", "/docentes/*"})
public class DocenteController extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(DocenteController.class);
    private DocenteDao docenteDao;
    
    @Override
    public void init() throws ServletException {
        docenteDao = new DocenteDaoImpl();
        logger.info("DocenteController inicializado");
    }
    
    /**
     * Maneja GET - Listar, ver detalle, o mostrar formulario
     */
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
    
    /**
     * Maneja POST - Crear, actualizar o eliminar
     */
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
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    /**
     * Lista todos los docentes
     */
    private void listarDocentes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Docente> docentes = docenteDao.listarTodos();
        
        request.setAttribute("docentes", docentes);
        request.setAttribute("totalDocentes", docentes.size());
        
        logger.debug("Listando {} docentes", docentes.size());
        
        request.getRequestDispatcher("/WEB-INF/views/docentes/listar.jsp").forward(request, response);
    }
    
    /**
     * Muestra formulario para crear nuevo docente
     */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
    }
    
    /**
     * Muestra formulario para editar docente
     */
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
                request.setAttribute("docente", docenteOpt.get());
                request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Docente no encontrado");
                listarDocentes(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        }
    }
    
    /**
     * Muestra detalle de un docente
     */
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
    
    /**
     * Crea un nuevo docente
     */
    private void crearDocente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener parámetros
        String dni = request.getParameter("dni");
        String nombre = request.getParameter("nombre");
        String apellidoPaterno = request.getParameter("apellidoPaterno");
        String apellidoMaterno = request.getParameter("apellidoMaterno");
        String email = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        
        // Validar campos obligatorios
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
        
        // Validar DNI
        if (!Validation.isValidDNI(dni)) {
            request.setAttribute("error", "DNI inválido. Debe tener 8 dígitos");
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            return;
        }
        
        // Validar email si fue proporcionado
        if (Validation.isNotEmpty(email) && !Validation.isValidEmail(email)) {
            request.setAttribute("error", "Email inválido");
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            return;
        }
        
        // Verificar si el DNI ya existe
        if (docenteDao.existeDni(dni)) {
            request.setAttribute("error", "Ya existe un docente con ese DNI");
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            return;
        }
        
        // Crear docente
        Docente docente = new Docente(dni, nombre, apellidoPaterno, apellidoMaterno);
        docente.setEmail(email);
        docente.setTelefono(telefono);
        
        boolean creado = docenteDao.insertar(docente);
        
        if (creado) {
            logger.info("Docente creado: {}", docente.getNombreCompleto());
            request.getSession().setAttribute("mensaje", "Docente creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        } else {
            request.setAttribute("error", "Error al crear el docente");
            request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
        }
    }
    
    /**
     * Actualiza un docente existente
     */
    private void actualizarDocente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            // Obtener parámetros
            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            String apellidoPaterno = request.getParameter("apellidoPaterno");
            String apellidoMaterno = request.getParameter("apellidoMaterno");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            
            // Validaciones similares a crear...
            
            // Buscar docente existente
            Optional<Docente> docenteOpt = docenteDao.buscarPorId(id);
            
            if (docenteOpt.isEmpty()) {
                request.setAttribute("error", "Docente no encontrado");
                listarDocentes(request, response);
                return;
            }
            
            Docente docente = docenteOpt.get();
            docente.setDni(dni);
            docente.setNombre(nombre);
            docente.setApellidoPaterno(apellidoPaterno);
            docente.setApellidoMaterno(apellidoMaterno);
            docente.setEmail(email);
            docente.setTelefono(telefono);
            
            boolean actualizado = docenteDao.actualizar(docente);
            
            if (actualizado) {
                logger.info("Docente actualizado: {}", docente.getNombreCompleto());
                request.getSession().setAttribute("mensaje", "Docente actualizado exitosamente");
                response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            } else {
                request.setAttribute("error", "Error al actualizar el docente");
                request.setAttribute("docente", docente);
                request.getRequestDispatcher("/WEB-INF/views/docentes/formulario.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        }
    }
    
    /**
     * Elimina un docente
     */
    private void eliminarDocente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            boolean eliminado = docenteDao.eliminar(id);
            
            if (eliminado) {
                logger.info("Docente eliminado con ID: {}", id);
                request.getSession().setAttribute("mensaje", "Docente eliminado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el docente");
            }
            
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/docentes?action=listar");
        }
    }
}