package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.DocenteDao;
import com.cwiesse.horarios.dao.impl.DocenteDaoImpl;
import com.cwiesse.horarios.model.Docente;
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
 * Servlet para exportar docentes a Excel.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
@WebServlet(name = "ExportDocentesServlet", urlPatterns = {"/docentes/exportar"})
public class ExportDocentesServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(ExportDocentesServlet.class);
    private DocenteDao docenteDao;
    
    @Override
    public void init() throws ServletException {
        docenteDao = new DocenteDaoImpl();
        logger.info("ExportDocentesServlet inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        logger.info("Solicitud de exportación de docentes a Excel");
        
        try {
            // Obtener todos los docentes
            List<Docente> docentes = docenteDao.listarTodos();
            
            if (docentes.isEmpty()) {
                logger.warn("No hay docentes para exportar");
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay docentes para exportar");
                return;
            }
            
            // Generar Excel
            ByteArrayOutputStream excelStream = ExcelExportService.exportarDocentesExcel(docentes);
            
            // Configurar respuesta para descarga
            String fileName = "Docentes_" + 
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
            
            logger.info("Excel generado y enviado: {} ({} bytes)", fileName, excelStream.size());
            
        } catch (Exception e) {
            logger.error("Error al exportar docentes: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error al generar el archivo Excel");
        }
    }
}