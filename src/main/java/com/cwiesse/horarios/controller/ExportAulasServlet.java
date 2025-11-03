package com.cwiesse.horarios.controller;

import com.cwiesse.horarios.dao.AulaDao;
import com.cwiesse.horarios.dao.impl.AulaDaoImpl;
import com.cwiesse.horarios.model.Aula;
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
 * Servlet para exportar aulas a Excel.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
@WebServlet(name = "ExportAulasServlet", urlPatterns = {"/aulas/exportar"})
public class ExportAulasServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(ExportAulasServlet.class);
    private AulaDao aulaDao;
    
    @Override
    public void init() throws ServletException {
        aulaDao = new AulaDaoImpl();
        logger.info("ExportAulasServlet inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        logger.info("Solicitud de exportación de aulas a Excel");
        
        try {
            // Obtener todas las aulas
            List<Aula> aulas = aulaDao.listarTodas();
            
            if (aulas.isEmpty()) {
                logger.warn("No hay aulas para exportar");
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay aulas para exportar");
                return;
            }
            
            // Generar Excel
            ByteArrayOutputStream excelStream = ExcelExportService.exportarAulasExcel(aulas);
            
            // Configurar respuesta para descarga
            String fileName = "Aulas_" + 
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
            
            logger.info("Excel de aulas generado y enviado: {} ({} bytes)", fileName, excelStream.size());
            
        } catch (Exception e) {
            logger.error("Error al exportar aulas: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error al generar el archivo Excel");
        }
    }
}