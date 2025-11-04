package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.HorarioDao;
import com.cwiesse.horarios.dao.impl.HorarioDaoImpl;
import com.cwiesse.horarios.model.Horario;
import com.cwiesse.horarios.service.ExcelExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servlet para exportar horarios a Excel.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
@WebServlet(name = "ExportHorariosServlet", urlPatterns = {"/horarios/exportar"})
public class ExportHorariosServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(ExportHorariosServlet.class);
    private HorarioDao horarioDao;
    
    @Override
    public void init() throws ServletException {
        horarioDao = new HorarioDaoImpl();
        logger.info("ExportHorariosServlet inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        logger.info("Solicitud de exportación de horarios a Excel");
        
        try {
            // Obtener todos los horarios
            List<Horario> horarios = horarioDao.listarTodos();
            
            if (horarios.isEmpty()) {
                logger.warn("No hay horarios para exportar");
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay horarios para exportar");
                return;
            }
            
            // Generar Excel
            ByteArrayOutputStream excelStream = ExcelExportService.exportarHorariosExcel(horarios);
            
            // Configurar respuesta para descarga
            String fileName = "Horarios_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                ".xlsx";
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentLength(excelStream.size());
            
            // Escribir archivo al response
            OutputStream out = response.getOutputStream();
            excelStream.writeTo(out);
            out.flush();
            out.close();
            
            logger.info("Excel de horarios generado y enviado: {} ({} bytes)", fileName, excelStream.size());
            
        } catch (Exception e) {
            logger.error("Error al exportar horarios: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error al generar el archivo Excel");
        }
    }
}