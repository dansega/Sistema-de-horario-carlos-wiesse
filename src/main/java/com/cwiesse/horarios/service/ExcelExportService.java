package com.cwiesse.horarios.service;

import com.cwiesse.horarios.model.Docente;
import com.cwiesse.horarios.model.Aula;
import com.cwiesse.horarios.model.Horario;
import com.cwiesse.horarios.model.Curso;
import org.apache.poi.ss.usermodel.*;
import com.cwiesse.horarios.model.Grado;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


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
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Docentes");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE DOCENTES - COLEGIO CARLOS WIESSE");
        CellStyle titleStyle = createTitleStyle(workbook);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
        
        Row dateRow = sheet.createRow(1);
        Cell dateCell = dateRow.createCell(0);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        dateCell.setCellValue("Fecha de generación: " + fecha);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 5));
        
        Row headerRow = sheet.createRow(3);
        String[] headers = {"DNI", "Nombre", "Apellido Paterno", "Apellido Materno", "Email", "Teléfono"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
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
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        logger.info("Excel generado exitosamente con {} registros", docentes.size());
        
        return outputStream;
    }
    
    /**
     * Exporta una lista de aulas a un archivo Excel (.xlsx)
     * 
     * @param aulas Lista de aulas a exportar
     * @return ByteArrayOutputStream con el contenido del Excel
     * @throws IOException Si hay error al crear el archivo
     */
    public static ByteArrayOutputStream exportarAulasExcel(List<Aula> aulas) throws IOException {
        logger.info("Iniciando exportación de {} aulas a Excel", aulas.size());
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Aulas");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE AULAS - COLEGIO CARLOS WIESSE");
        CellStyle titleStyle = createTitleStyle(workbook);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));
        
        Row dateRow = sheet.createRow(1);
        Cell dateCell = dateRow.createCell(0);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        dateCell.setCellValue("Fecha de generación: " + fecha);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 5));
        
        Row headerRow = sheet.createRow(3);
        String[] headers = {"Código", "Nombre", "Capacidad", "Piso", "Edificio", "Estado"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
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
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        logger.info("Excel de aulas generado exitosamente con {} registros", aulas.size());
        
        return outputStream;
    }
    
    /**
     * Exporta una lista de cursos a un archivo Excel (.xlsx)
     * 
     * @param cursos Lista de cursos a exportar
     * @return ByteArrayOutputStream con el contenido del Excel
     * @throws IOException Si hay error al crear el archivo
     */
    public static ByteArrayOutputStream exportarCursosExcel(List<Curso> cursos) throws IOException {
        logger.info("Iniciando exportación de {} cursos a Excel", cursos.size());
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cursos");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE CURSOS - COLEGIO CARLOS WIESSE");
        CellStyle titleStyle = createTitleStyle(workbook);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));
        
        Row dateRow = sheet.createRow(1);
        Cell dateCell = dateRow.createCell(0);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        dateCell.setCellValue("Fecha de generación: " + fecha);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 3));
        
        Row headerRow = sheet.createRow(3);
        String[] headers = {"Nombre", "Descripción", "Horas Semanales", "Estado"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        int rowNum = 4;
        for (Curso curso : cursos) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(curso.getNombre());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(curso.getDescripcion() != null ? curso.getDescripcion() : "");
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(curso.getHorasSemanales() + "h");
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(curso.isEstado() ? "Activo" : "Inactivo");
            cell3.setCellStyle(dataStyle);
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        logger.info("Excel de cursos generado exitosamente con {} registros", cursos.size());
        
        return outputStream;
    }
    
    /**
     * Exporta una lista de horarios a un archivo Excel (.xlsx)
     * 
     * @param horarios Lista de horarios a exportar
     * @return ByteArrayOutputStream con el contenido del Excel
     * @throws IOException Si hay error al crear el archivo
     */
    public static ByteArrayOutputStream exportarHorariosExcel(List<Horario> horarios) throws IOException {
        logger.info("Iniciando exportación de {} horarios a Excel", horarios.size());
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Horarios");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("REPORTE DE HORARIOS - COLEGIO CARLOS WIESSE");
        CellStyle titleStyle = createTitleStyle(workbook);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 6));
        
        Row dateRow = sheet.createRow(1);
        Cell dateCell = dateRow.createCell(0);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        dateCell.setCellValue("Fecha de generación: " + fecha);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 6));
        
        Row headerRow = sheet.createRow(3);
        String[] headers = {"Día", "Hora Inicio", "Hora Fin", "Duración", "Docente", "Curso", "Aula"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        int rowNum = 4;
        for (Horario horario : horarios) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(horario.getDia().getNombre());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(horario.getHoraInicio().toString());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(horario.getHoraFin().toString());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(horario.getDuracionMinutos() + " min");
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(horario.getDocente().getNombreCompleto());
            cell4.setCellStyle(dataStyle);
            
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(horario.getCurso().getNombre());
            cell5.setCellStyle(dataStyle);
            
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(horario.getAula().getCodigo());
            cell6.setCellStyle(dataStyle);
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        logger.info("Excel de horarios generado exitosamente con {} registros", horarios.size());
        
        return outputStream;
    }
    
    /**
     * Exporta el horario personal de un docente a un archivo Excel (.xlsx)
     * 
     * @param horarios Lista de horarios del docente
     * @param nombreDocente Nombre completo del docente
     * @return ByteArrayOutputStream con el contenido del Excel
     * @throws IOException Si hay error al crear el archivo
     */
    public static ByteArrayOutputStream exportarHorarioDocenteExcel(List<Horario> horarios, String nombreDocente) throws IOException {
        logger.info("Iniciando exportación de horario personal para: {}", nombreDocente);
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Mi Horario");
        
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("MI HORARIO SEMANAL - " + nombreDocente.toUpperCase());
        CellStyle titleStyle = createTitleStyle(workbook);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 4));
        
        Row dateRow = sheet.createRow(1);
        Cell dateCell = dateRow.createCell(0);
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        dateCell.setCellValue("Fecha de generación: " + fecha);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 4));
        
        Row headerRow = sheet.createRow(3);
        String[] headers = {"Día", "Hora Inicio", "Hora Fin", "Curso", "Aula"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        int rowNum = 4;
        for (Horario horario : horarios) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(horario.getDia().getNombre());
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(horario.getHoraInicio().toString());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(horario.getHoraFin().toString());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(horario.getCurso().getNombre());
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(horario.getAula().getCodigo() + 
                              (horario.getAula().getNombre() != null ? " - " + horario.getAula().getNombre() : ""));
            cell4.setCellStyle(dataStyle);
        }
        
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1500);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        logger.info("Excel de horario personal generado exitosamente con {} clases", horarios.size());
        
        return outputStream;
    }
    
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
 * Exporta una lista de grados a un archivo Excel (.xlsx)
 * 
 * @param grados Lista de grados a exportar
 * @return ByteArrayOutputStream con el contenido del Excel
 * @throws IOException Si hay error al crear el archivo
 */
public static ByteArrayOutputStream exportarGradosExcel(List<Grado> grados) throws IOException {
    logger.info("Iniciando exportación de {} grados a Excel", grados.size());
    
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Grados");
    
    CellStyle headerStyle = createHeaderStyle(workbook);
    CellStyle dataStyle = createDataStyle(workbook);
    
    Row titleRow = sheet.createRow(0);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue("REPORTE DE GRADOS/SECCIONES - COLEGIO CARLOS WIESSE");
    CellStyle titleStyle = createTitleStyle(workbook);
    titleCell.setCellStyle(titleStyle);
    sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 4));
    
    Row dateRow = sheet.createRow(1);
    Cell dateCell = dateRow.createCell(0);
    String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    dateCell.setCellValue("Fecha de generación: " + fecha);
    sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 4));
    
    Row headerRow = sheet.createRow(3);
    String[] headers = {"Nivel", "Grado", "Sección", "Aula Asignada", "Estado"};
    
    for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
    }
    
    int rowNum = 4;
    for (Grado grado : grados) {
        Row row = sheet.createRow(rowNum++);
        
        Cell cell0 = row.createCell(0);
        cell0.setCellValue(grado.getNivel().getNombre());
        cell0.setCellStyle(dataStyle);
        
        Cell cell1 = row.createCell(1);
        cell1.setCellValue(grado.getNumero() + "°");
        cell1.setCellStyle(dataStyle);
        
        Cell cell2 = row.createCell(2);
        cell2.setCellValue(grado.getSeccion());
        cell2.setCellStyle(dataStyle);
        
        Cell cell3 = row.createCell(3);
        if (grado.getAula() != null) {
            cell3.setCellValue(grado.getAula().getCodigo() + 
                              (grado.getAula().getNombre() != null ? " - " + grado.getAula().getNombre() : ""));
        } else {
            cell3.setCellValue("Sin asignar");
        }
        cell3.setCellStyle(dataStyle);
        
        Cell cell4 = row.createCell(4);
        cell4.setCellValue(grado.isEstado() ? "Activo" : "Inactivo");
        cell4.setCellStyle(dataStyle);
    }
    
    for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
        sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
    }
    
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();
    
    logger.info("Excel de grados generado exitosamente con {} registros", grados.size());
    
    return outputStream;
}
}