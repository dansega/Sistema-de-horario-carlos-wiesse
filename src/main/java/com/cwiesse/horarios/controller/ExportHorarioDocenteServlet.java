package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.DocenteDao;
import com.cwiesse.horarios.dao.HorarioDao;
import com.cwiesse.horarios.dao.impl.DocenteDaoImpl;
import com.cwiesse.horarios.dao.impl.HorarioDaoImpl;
import com.cwiesse.horarios.model.Docente;
import com.cwiesse.horarios.model.Horario;
import com.cwiesse.horarios.model.Usuario;
import com.cwiesse.horarios.service.ExcelExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Servlet para exportar el horario personal del docente a Excel.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
@WebServlet(name = "ExportHorarioDocenteServlet", urlPatterns = {"/docente/horario/exportar"})
public class ExportHorarioDocenteServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(ExportHorarioDocenteServlet.class);
    private DocenteDao docenteDao;
    private HorarioDao horarioDao;
    
    @Override
    public void init() throws ServletException {
        docenteDao = new DocenteDaoImpl();
        horarioDao = new HorarioDaoImpl();
        logger.info("ExportHorarioDocenteServlet inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario.getRol() != Usuario.Rol.DOCENTE) {
            logger.warn("Usuario {} con rol {} intentó exportar horario de docente", 
                       usuario.getUsername(), usuario.getRol());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        try {
            Optional<Docente> docenteOpt = docenteDao.buscarPorUsuarioId(usuario.getId());
            
            if (docenteOpt.isEmpty()) {
                logger.error("No se encontró docente para usuario: {}", usuario.getUsername());
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Docente no encontrado");
                return;
            }
            
            Docente docente = docenteOpt.get();
            
            List<Horario> horarios = horarioDao.listarPorDocente(docente.getId());
            
            if (horarios.isEmpty()) {
                logger.warn("No hay horarios para exportar del docente: {}", docente.getNombreCompleto());
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No tienes clases asignadas");
                return;
            }
            
            ByteArrayOutputStream excelStream = ExcelExportService.exportarHorarioDocenteExcel(
                horarios, 
                docente.getNombreCompleto()
            );
            
            String fileName = "MiHorario_" + 
                docente.getApellidoPaterno() + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                ".xlsx";
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentLength(excelStream.size());
            
            OutputStream out = response.getOutputStream();
            excelStream.writeTo(out);
            out.flush();
            out.close();
            
            logger.info("Excel de horario personal generado: {} ({} bytes)", fileName, excelStream.size());
            
        } catch (Exception e) {
            logger.error("Error al exportar horario del docente: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error al generar el archivo Excel");
        }
    }
}