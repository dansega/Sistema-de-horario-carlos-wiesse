package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.GradoDao;
import com.cwiesse.horarios.dao.AulaDao;
import com.cwiesse.horarios.dao.impl.GradoDaoImpl;
import com.cwiesse.horarios.dao.impl.AulaDaoImpl;
import com.cwiesse.horarios.model.Grado;
import com.cwiesse.horarios.model.Aula;
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

@WebServlet(name = "GradoController", urlPatterns = {"/grados", "/grados/*"})
public class GradoController extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(GradoController.class);
    private GradoDao gradoDao;
    private AulaDao aulaDao;
    
    @Override
    public void init() throws ServletException {
        gradoDao = new GradoDaoImpl();
        aulaDao = new AulaDaoImpl();
        logger.info("GradoController inicializado");
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
                listarGrados(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            default:
                listarGrados(request, response);
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
                crearGrado(request, response);
                break;
            case "actualizar":
                actualizarGrado(request, response);
                break;
            case "eliminar":
                eliminarGrado(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void listarGrados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Grado> grados = gradoDao.listarTodos();
        
        request.setAttribute("grados", grados);
        request.setAttribute("totalGrados", grados.size());
        
        logger.debug("Listando {} grados", grados.size());
        
        request.getRequestDispatcher("/WEB-INF/views/grados/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cargar lista de aulas para el select
        List<Aula> aulas = aulaDao.listarActivas();
        request.setAttribute("aulas", aulas);
        
        // Cargar array de niveles
        request.setAttribute("niveles", Grado.Nivel.values());
        
        request.getRequestDispatcher("/WEB-INF/views/grados/formulario.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/grados?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            Optional<Grado> gradoOpt = gradoDao.buscarPorId(id);
            
            if (gradoOpt.isPresent()) {
                request.setAttribute("grado", gradoOpt.get());
                
                // Cargar lista de aulas para el select
                List<Aula> aulas = aulaDao.listarActivas();
                request.setAttribute("aulas", aulas);
                
                // Cargar array de niveles
                request.setAttribute("niveles", Grado.Nivel.values());
                
                request.getRequestDispatcher("/WEB-INF/views/grados/formulario.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Grado no encontrado");
                listarGrados(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/grados?action=listar");
        }
    }
    
    private void crearGrado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nivelStr = request.getParameter("nivel");
        String numeroStr = request.getParameter("numero");
        String seccion = request.getParameter("seccion");
        String aulaIdStr = request.getParameter("aulaId");
        
        // Validaciones
        if (!Validation.isNotEmpty(nivelStr) || !Validation.isNotEmpty(numeroStr) || 
            !Validation.isNotEmpty(seccion)) {
            request.setAttribute("error", "Todos los campos son obligatorios");
            mostrarFormularioNuevo(request, response);
            return;
        }
        
        // Validar número de grado
        Integer numero;
        try {
            numero = Integer.parseInt(numeroStr);
            if (numero < 1 || numero > 6) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El número de grado debe estar entre 1 y 6");
            mostrarFormularioNuevo(request, response);
            return;
        }
        
        // Validar nivel
        Grado.Nivel nivel;
        try {
            nivel = Grado.Nivel.valueOf(nivelStr);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Nivel inválido");
            mostrarFormularioNuevo(request, response);
            return;
        }
        
        // Validar sección
        seccion = seccion.trim().toUpperCase();
        if (seccion.length() > 10) {
            request.setAttribute("error", "La sección no puede tener más de 10 caracteres");
            mostrarFormularioNuevo(request, response);
            return;
        }
        
        // Verificar que no exista el grado
        if (gradoDao.existeGrado(nivel, numero, seccion)) {
            request.setAttribute("error", "Ya existe un grado " + numero + "° " + seccion + " - " + nivel.getNombre());
            mostrarFormularioNuevo(request, response);
            return;
        }
        
        // Procesar aula (opcional)
        Integer aulaId = null;
        if (aulaIdStr != null && !aulaIdStr.trim().isEmpty() && !aulaIdStr.equals("")) {
            try {
                aulaId = Integer.parseInt(aulaIdStr);
            } catch (NumberFormatException e) {
                // Ignorar si no es válido
            }
        }
        
        // Crear grado
        Grado grado = new Grado(nivel, numero, seccion, aulaId);
        
        boolean creado = gradoDao.insertar(grado);
        
        if (creado) {
            logger.info("Grado creado: {}", grado.getNombreCompleto());
            request.getSession().setAttribute("mensaje", "Grado creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/grados?action=listar");
        } else {
            request.setAttribute("error", "Error al crear el grado");
            mostrarFormularioNuevo(request, response);
        }
    }
    
    private void actualizarGrado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/grados?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            String nivelStr = request.getParameter("nivel");
            String numeroStr = request.getParameter("numero");
            String seccion = request.getParameter("seccion");
            String aulaIdStr = request.getParameter("aulaId");
            
            Optional<Grado> gradoOpt = gradoDao.buscarPorId(id);
            
            if (gradoOpt.isEmpty()) {
                request.setAttribute("error", "Grado no encontrado");
                listarGrados(request, response);
                return;
            }
            
            Grado grado = gradoOpt.get();
            
            // Validaciones
            if (!Validation.isNotEmpty(nivelStr) || !Validation.isNotEmpty(numeroStr) || 
                !Validation.isNotEmpty(seccion)) {
                request.setAttribute("error", "Todos los campos son obligatorios");
                request.setAttribute("grado", grado);
                mostrarFormularioEditar(request, response);
                return;
            }
            
            // Validar número de grado
            Integer numero;
            try {
                numero = Integer.parseInt(numeroStr);
                if (numero < 1 || numero > 6) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "El número de grado debe estar entre 1 y 6");
                request.setAttribute("grado", grado);
                mostrarFormularioEditar(request, response);
                return;
            }
            
            // Validar nivel
            Grado.Nivel nivel;
            try {
                nivel = Grado.Nivel.valueOf(nivelStr);
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Nivel inválido");
                request.setAttribute("grado", grado);
                mostrarFormularioEditar(request, response);
                return;
            }
            
            // Validar sección
            seccion = seccion.trim().toUpperCase();
            if (seccion.length() > 10) {
                request.setAttribute("error", "La sección no puede tener más de 10 caracteres");
                request.setAttribute("grado", grado);
                mostrarFormularioEditar(request, response);
                return;
            }
            
            // Verificar que no exista otro grado con esa combinación
            if (gradoDao.existeGradoExceptoId(nivel, numero, seccion, id)) {
                request.setAttribute("error", "Ya existe otro grado " + numero + "° " + seccion + " - " + nivel.getNombre());
                request.setAttribute("grado", grado);
                mostrarFormularioEditar(request, response);
                return;
            }
            
            // Procesar aula (opcional)
            Integer aulaId = null;
            if (aulaIdStr != null && !aulaIdStr.trim().isEmpty() && !aulaIdStr.equals("")) {
                try {
                    aulaId = Integer.parseInt(aulaIdStr);
                } catch (NumberFormatException e) {
                    // Ignorar si no es válido
                }
            }
            
            grado.setNivel(nivel);
            grado.setNumero(numero);
            grado.setSeccion(seccion);
            grado.setAulaId(aulaId);
            
            if (gradoDao.actualizar(grado)) {
                request.getSession().setAttribute("mensaje", "Grado actualizado exitosamente");
                response.sendRedirect(request.getContextPath() + "/grados?action=listar");
            } else {
                request.setAttribute("error", "Error al actualizar el grado");
                request.setAttribute("grado", grado);
                mostrarFormularioEditar(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error al actualizar grado", e);
            request.setAttribute("error", "ERROR: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/grados?action=listar");
        }
    }
    
    private void eliminarGrado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/grados?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            boolean eliminado = gradoDao.eliminar(id);
            
            if (eliminado) {
                logger.info("Grado eliminado con ID: {}", id);
                request.getSession().setAttribute("mensaje", "Grado eliminado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el grado");
            }
            
            response.sendRedirect(request.getContextPath() + "/grados?action=listar");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/grados?action=listar");
        }
    }
}