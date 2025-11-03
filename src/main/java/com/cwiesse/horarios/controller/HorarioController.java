package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.HorarioDao;
import com.cwiesse.horarios.dao.DocenteDao;
import com.cwiesse.horarios.dao.AulaDao;
import com.cwiesse.horarios.dao.CursoDao;
import com.cwiesse.horarios.dao.impl.HorarioDaoImpl;
import com.cwiesse.horarios.dao.impl.DocenteDaoImpl;
import com.cwiesse.horarios.dao.impl.AulaDaoImpl;
import com.cwiesse.horarios.dao.impl.CursoDaoImpl;
import com.cwiesse.horarios.model.Horario;
import com.cwiesse.horarios.model.Docente;
import com.cwiesse.horarios.model.Aula;
import com.cwiesse.horarios.model.Curso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Servlet para manejar el CRUD de horarios con validación de choques.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
@WebServlet(name = "HorarioController", urlPatterns = {"/horarios", "/horarios/*"})
public class HorarioController extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(HorarioController.class);
    private HorarioDao horarioDao;
    private DocenteDao docenteDao;
    private AulaDao aulaDao;
    private CursoDao cursoDao;
    
    @Override
    public void init() throws ServletException {
        horarioDao = new HorarioDaoImpl();
        docenteDao = new DocenteDaoImpl();
        aulaDao = new AulaDaoImpl();
        cursoDao = new CursoDaoImpl();
        logger.info("HorarioController inicializado");
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
                listarHorarios(request, response);
                break;
            case "nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "editar":
                mostrarFormularioEditar(request, response);
                break;
            default:
                listarHorarios(request, response);
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
                crearHorario(request, response);
                break;
            case "actualizar":
                actualizarHorario(request, response);
                break;
            case "eliminar":
                eliminarHorario(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    
    private void listarHorarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Horario> horarios = horarioDao.listarTodos();
        
        request.setAttribute("horarios", horarios);
        request.setAttribute("totalHorarios", horarios.size());
        
        logger.debug("Listando {} horarios", horarios.size());
        
        request.getRequestDispatcher("/WEB-INF/views/horarios/listar.jsp").forward(request, response);
    }
    
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Cargar listas para los combos
        cargarDatosFormulario(request);
        
        request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
    }
    
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/horarios?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            Optional<Horario> horarioOpt = horarioDao.buscarPorId(id);
            
            if (horarioOpt.isPresent()) {
                request.setAttribute("horario", horarioOpt.get());
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Horario no encontrado");
                listarHorarios(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/horarios?action=listar");
        }
    }
    
    private void crearHorario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtener parámetros
            Integer docenteId = Integer.parseInt(request.getParameter("docenteId"));
            Integer aulaId = Integer.parseInt(request.getParameter("aulaId"));
            Integer cursoId = Integer.parseInt(request.getParameter("cursoId"));
            String diaStr = request.getParameter("dia");
            String horaInicioStr = request.getParameter("horaInicio");
            String horaFinStr = request.getParameter("horaFin");
            
            // Validar campos
            if (diaStr == null || horaInicioStr == null || horaFinStr == null) {
                request.setAttribute("error", "Todos los campos son obligatorios");
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
                return;
            }
            
            LocalTime horaInicio = LocalTime.parse(horaInicioStr);
            LocalTime horaFin = LocalTime.parse(horaFinStr);
            
            // Validar que hora inicio < hora fin
            if (!horaInicio.isBefore(horaFin)) {
                request.setAttribute("error", "La hora de inicio debe ser menor que la hora de fin");
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
                return;
            }
            
            // Validar choque de docente
            if (horarioDao.existeChoqueDocente(docenteId, diaStr, horaInicioStr, horaFinStr, null)) {
                request.setAttribute("error", "El docente ya tiene un horario asignado en ese día y hora");
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
                return;
            }
            
            // Validar choque de aula
            if (horarioDao.existeChoqueAula(aulaId, diaStr, horaInicioStr, horaFinStr, null)) {
                request.setAttribute("error", "El aula ya está ocupada en ese día y hora");
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
                return;
            }
            
            // Crear horario
            Horario horario = new Horario();
            horario.setDocenteId(docenteId);
            horario.setAulaId(aulaId);
            horario.setCursoId(cursoId);
            horario.setDia(Horario.Dia.valueOf(diaStr));
            horario.setHoraInicio(horaInicio);
            horario.setHoraFin(horaFin);
            
            boolean creado = horarioDao.insertar(horario);
            
            if (creado) {
                logger.info("Horario creado: {} {}-{}", diaStr, horaInicioStr, horaFinStr);
                request.getSession().setAttribute("mensaje", "Horario creado exitosamente");
                response.sendRedirect(request.getContextPath() + "/horarios?action=listar");
            } else {
                request.setAttribute("error", "Error al crear el horario");
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error al crear horario: {}", e.getMessage());
            request.setAttribute("error", "Error al procesar los datos: " + e.getMessage());
            cargarDatosFormulario(request);
            request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
        }
    }
    
    private void actualizarHorario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/horarios?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            Integer docenteId = Integer.parseInt(request.getParameter("docenteId"));
            Integer aulaId = Integer.parseInt(request.getParameter("aulaId"));
            Integer cursoId = Integer.parseInt(request.getParameter("cursoId"));
            String diaStr = request.getParameter("dia");
            String horaInicioStr = request.getParameter("horaInicio");
            String horaFinStr = request.getParameter("horaFin");
            
            LocalTime horaInicio = LocalTime.parse(horaInicioStr);
            LocalTime horaFin = LocalTime.parse(horaFinStr);
            
            // Validar que hora inicio < hora fin
            if (!horaInicio.isBefore(horaFin)) {
                request.setAttribute("error", "La hora de inicio debe ser menor que la hora de fin");
                Optional<Horario> horarioOpt = horarioDao.buscarPorId(id);
                if (horarioOpt.isPresent()) {
                    request.setAttribute("horario", horarioOpt.get());
                }
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
                return;
            }
            
            // Validar choque de docente (excluyendo este horario)
            if (horarioDao.existeChoqueDocente(docenteId, diaStr, horaInicioStr, horaFinStr, id)) {
                request.setAttribute("error", "El docente ya tiene un horario asignado en ese día y hora");
                Optional<Horario> horarioOpt = horarioDao.buscarPorId(id);
                if (horarioOpt.isPresent()) {
                    request.setAttribute("horario", horarioOpt.get());
                }
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
                return;
            }
            
            // Validar choque de aula (excluyendo este horario)
            if (horarioDao.existeChoqueAula(aulaId, diaStr, horaInicioStr, horaFinStr, id)) {
                request.setAttribute("error", "El aula ya está ocupada en ese día y hora");
                Optional<Horario> horarioOpt = horarioDao.buscarPorId(id);
                if (horarioOpt.isPresent()) {
                    request.setAttribute("horario", horarioOpt.get());
                }
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
                return;
            }
            
            Optional<Horario> horarioOpt = horarioDao.buscarPorId(id);
            
            if (horarioOpt.isEmpty()) {
                request.setAttribute("error", "Horario no encontrado");
                listarHorarios(request, response);
                return;
            }
            
            Horario horario = horarioOpt.get();
            horario.setDocenteId(docenteId);
            horario.setAulaId(aulaId);
            horario.setCursoId(cursoId);
            horario.setDia(Horario.Dia.valueOf(diaStr));
            horario.setHoraInicio(horaInicio);
            horario.setHoraFin(horaFin);
            
            boolean actualizado = horarioDao.actualizar(horario);
            
            if (actualizado) {
                logger.info("Horario actualizado: ID {}", id);
                request.getSession().setAttribute("mensaje", "Horario actualizado exitosamente");
                response.sendRedirect(request.getContextPath() + "/horarios?action=listar");
            } else {
                request.setAttribute("error", "Error al actualizar el horario");
                request.setAttribute("horario", horario);
                cargarDatosFormulario(request);
                request.getRequestDispatcher("/WEB-INF/views/horarios/formulario.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.error("Error al actualizar horario: {}", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/horarios?action=listar");
        }
    }
    
    private void eliminarHorario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/horarios?action=listar");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idStr);
            
            boolean eliminado = horarioDao.eliminar(id);
            
            if (eliminado) {
                logger.info("Horario eliminado con ID: {}", id);
                request.getSession().setAttribute("mensaje", "Horario eliminado exitosamente");
            } else {
                request.getSession().setAttribute("error", "Error al eliminar el horario");
            }
            
            response.sendRedirect(request.getContextPath() + "/horarios?action=listar");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/horarios?action=listar");
        }
    }
    
    /**
     * Carga las listas necesarias para los combos del formulario
     */
    private void cargarDatosFormulario(HttpServletRequest request) {
        List<Docente> docentes = docenteDao.listarActivos();
        List<Aula> aulas = aulaDao.listarActivas();
        List<Curso> cursos = cursoDao.listarActivos();
        
        request.setAttribute("docentes", docentes);
        request.setAttribute("aulas", aulas);
        request.setAttribute("cursos", cursos);
        request.setAttribute("dias", Horario.Dia.values());
    }
}