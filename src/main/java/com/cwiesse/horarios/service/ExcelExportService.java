package com.cwiesse.horarios.service;

import com.cwiesse.horarios.model.Docente;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
importar horarios del modelo aula
*/
import com.cwiesse.horarios.model.Aula;

/**
 * Servicio para exportar datos a Excel usando Apache POI.
 * 
 * @author Carlos Wiesse
 * @version 1.0
 */
public class ExcelExportService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);
    
    /**
     * Exporta una lista de docentes a un archivo Excel (.xlsx)
     * 
     * @param docentes Lista de docentes a exportar
     * @return ByteArrayOutputStream con el contenido del Excel
     * @throws IOException Si hay error al crear el archivo
     */
    public static ByteArrayOutputStream exportarDocentesExcel(List<Docente> docentes) throws IOException {
        logger.info("Iniciando exportación de {} docentes a Excel", docentes.size());
        
        // Crear workbook y hoja
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Docentes");
        
        // Crear estilos
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        // Crear fila de título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE DOCENTES - COLEGIO CARLOS WIESSE");
        CellStyle titleStyle = createTitleStyle(workbook);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
        
        // Crear fila de fecha
        Row dateRow = sheet.createRow(1);
        Cell dateCell = dateRow.createCell(0);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        dateCell.setCellValue("Fecha de generación: " + fecha);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 5));
        
        // Crear encabezados (fila 3)
        Row headerRow = sheet.createRow(3);
        String[] headers = {"DNI", "Nombre", "Apellido Paterno", "Apellido Materno", "Email", "Teléfono"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos (desde fila 4)
        int rowNum = 4;
        for (Docente docente : docentes) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(docente.getDni());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(docente.getNombre());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(docente.getApellidoPaterno());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(docente.getApellidoMaterno());
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(docente.getEmail() != null ? docente.getEmail() : "");
            cell4.setCellStyle(dataStyle);
            
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(docente.getTelefono() != null ? docente.getTelefono() : "");
            cell5.setCellStyle(dataStyle);
        }
        
        // Autoajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            // Agregar un poco más de ancho
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
        
        // Escribir a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        logger.info("Excel generado exitosamente con {} registros", docentes.size());
        
        return outputStream;
    }
    
    /**
     * Crea estilo para el título
     */
    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return style;
    }
    
    /**
     * Crea estilo para los encabezados
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return style;
    }
    
    /**
     * Crea estilo para los datos
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return style;
    }
    
    /**
     *  codigo para exportar en excel lista de AULA
     */
    /**
     * Exporta una lista de aulas a un archivo Excel (.xlsx)
     * 
     * @param aulas Lista de aulas a exportar
     * @return ByteArrayOutputStream con el contenido del Excel
     * @throws IOException Si hay error al crear el archivo
     */
    public static ByteArrayOutputStream exportarAulasExcel(List<Aula> aulas) throws IOException {
        logger.info("Iniciando exportación de {} aulas a Excel", aulas.size());
        
        // Crear workbook y hoja
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Aulas");
        
        // Crear estilos
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        // Crear fila de título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE AULAS - COLEGIO CARLOS WIESSE");
        CellStyle titleStyle = createTitleStyle(workbook);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
        
        // Crear fila de fecha
        Row dateRow = sheet.createRow(1);
        Cell dateCell = dateRow.createCell(0);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        dateCell.setCellValue("Fecha de generación: " + fecha);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 5));
        
        // Crear encabezados (fila 3)
        Row headerRow = sheet.createRow(3);
        String[] headers = {"Código", "Nombre", "Capacidad", "Piso", "Edificio", "Estado"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos (desde fila 4)
        int rowNum = 4;
        for (Aula aula : aulas) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(aula.getCodigo());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(aula.getNombre() != null ? aula.getNombre() : "");
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(aula.getCapacidad() + " alumnos");
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue("Piso " + aula.getPiso());
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(aula.getEdificio() != null ? aula.getEdificio() : "");
            cell4.setCellStyle(dataStyle);
            
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(aula.isEstado() ? "Activa" : "Inactiva");
            cell5.setCellStyle(dataStyle);
        }
        
        // Autoajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
        
        // Escribir a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        logger.info("Excel de aulas generado exitosamente con {} registros", aulas.size());
        
        return outputStream;
    }
}