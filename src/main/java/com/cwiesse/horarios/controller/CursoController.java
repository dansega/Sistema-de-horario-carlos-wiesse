package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.CursoDao;
import com.cwiesse.horarios.dao.impl.CursoDaoImpl;
import com.cwiesse.horarios.model.Curso;
import com.cwiesse.horarios.util.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "CursoController", urlPatterns = {"/cursos", "/cursos/*"})
public class CursoController extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);
    private CursoDao cursoDao;
    
    @Override
    public void init() throws ServletException {
        cursoDao = new CursoDaoImpl();
        logger.info("CursoController inicializado");
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
                listarCursos(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            case "exportar":
                exportarCursosExcel(request, response);
                break;
            default:
                listarCursos(request, response);
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
                crearCurso(request, response);
                break;
            case "actualizar":
                actualizarCurso(request, response);
                break;
            case "eliminar":
                eliminarCurso(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void listarCursos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Curso> cursos = cursoDao.listarTodos();
        
        request.setAttribute("cursos", cursos);
        request.setAttribute("totalCursos", cursos.size());
        
        logger.debug("Listando {} cursos", cursos.size());
        
        request.getRequestDispatcher("/WEB-INF/views/cursos/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            Optional<Curso> cursoOpt = cursoDao.buscarPorId(id);
            
            if (cursoOpt.isPresent()) {
                request.setAttribute("curso", cursoOpt.get());
                request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Curso no encontrado");
                listarCursos(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
        }
    }
    
    private void crearCurso(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String horasSemanalesStr = request.getParameter("horasSemanales");
        
        // Validaciones
        if (!Validation.isNotEmpty(nombre)) {
            request.setAttribute("error", "El nombre del curso es obligatorio");
            request.setAttribute("nombre", nombre);
            request.setAttribute("descripcion", descripcion);
            request.setAttribute("horasSemanales", horasSemanalesStr);
            request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
            return;
        }
        
        // Validar horas semanales
        Integer horasSemanales;
        try {
            horasSemanales = Integer.parseInt(horasSemanalesStr);
            if (horasSemanales < 1 || horasSemanales > 10) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Las horas semanales deben ser un número entre 1 y 10");
            request.setAttribute("nombre", nombre);
            request.setAttribute("descripcion", descripcion);
            request.setAttribute("horasSemanales", horasSemanalesStr);
            request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
            return;
        }
        
        // Verificar que no exista un curso con el mismo nombre
        if (cursoDao.existeNombre(nombre)) {
            request.setAttribute("error", "Ya existe un curso con ese nombre");
            request.setAttribute("nombre", nombre);
            request.setAttribute("descripcion", descripcion);
            request.setAttribute("horasSemanales", horasSemanalesStr);
            request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
            return;
        }
        
        // Crear curso
        Curso curso = new Curso(nombre, descripcion, horasSemanales);
        
        boolean creado = cursoDao.insertar(curso);
        
        if (creado) {
            logger.info("Curso creado: {}", curso.getNombre());
            request.getSession().setAttribute("mensaje", "Curso creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
        } else {
            request.setAttribute("error", "Error al crear el curso");
            request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
        }
    }
    
    private void actualizarCurso(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String horasSemanalesStr = request.getParameter("horasSemanales");
            
            Optional<Curso> cursoOpt = cursoDao.buscarPorId(id);
            
            if (cursoOpt.isEmpty()) {
                request.setAttribute("error", "Curso no encontrado");
                listarCursos(request, response);
                return;
            }
            
            Curso curso = cursoOpt.get();
            
            // Validaciones
            if (!Validation.isNotEmpty(nombre)) {
                request.setAttribute("error", "El nombre del curso es obligatorio");
                request.setAttribute("curso", curso);
                request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
                return;
            }
            
            // Validar horas semanales
            Integer horasSemanales;
            try {
                horasSemanales = Integer.parseInt(horasSemanalesStr);
                if (horasSemanales < 1 || horasSemanales > 10) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Las horas semanales deben ser un número entre 1 y 10");
                request.setAttribute("curso", curso);
                request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
                return;
            }
            
            // Verificar que no exista otro curso con el mismo nombre
            if (cursoDao.existeNombreExceptoId(nombre, id)) {
                request.setAttribute("error", "Ya existe otro curso con ese nombre");
                request.setAttribute("curso", curso);
                request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
                return;
            }
            
            curso.setNombre(nombre);
            curso.setDescripcion(descripcion);
            curso.setHorasSemanales(horasSemanales);
            
            if (cursoDao.actualizar(curso)) {
                request.getSession().setAttribute("mensaje", "Curso actualizado exitosamente");
                response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
            } else {
                request.setAttribute("error", "Error al actualizar el curso");
                request.setAttribute("curso", curso);
                request.getRequestDispatcher("/WEB-INF/views/cursos/formulario.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error al actualizar curso", e);
            request.setAttribute("error", "ERROR: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
        }
    }
    
    private void eliminarCurso(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            boolean eliminado = cursoDao.eliminar(id);
            
            if (eliminado) {
                logger.info("Curso eliminado con ID: {}", id);
                request.getSession().setAttribute("mensaje", "Curso eliminado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el curso");
            }
            
            response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
        }
    }
    
    private void exportarCursosExcel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Curso> cursos = cursoDao.listarTodos();
        
        try {
            ByteArrayOutputStream outputStream = com.cwiesse.horarios.service.ExcelExportService.exportarCursosExcel(cursos);
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=cursos_" + 
                              java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");
            
            response.getOutputStream().write(outputStream.toByteArray());
            response.getOutputStream().flush();
            
            logger.info("Cursos exportados a Excel exitosamente");
            
        } catch (Exception e) {
            logger.error("Error al exportar cursos a Excel: {}", e.getMessage());
            request.getSession().setAttribute("error", "Error al generar el archivo Excel");
            response.sendRedirect(request.getContextPath() + "/cursos?action=listar");
        }
    }
}