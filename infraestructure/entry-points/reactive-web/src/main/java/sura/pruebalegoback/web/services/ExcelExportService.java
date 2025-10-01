package sura.pruebalegoback.web.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Service
public class ExcelExportService {

    /**
     * Genera un archivo Excel genérico a partir de una lista de datos.
     * @param <T> Tipo de los datos a exportar
     * @param data Lista de datos
     * @param columns Nombres de las columnas
     * @param rowMapper Función que mapea un objeto T a una lista de valores de celda (en orden de columnas)
     * @param sheetName Nombre de la hoja (opcional, por defecto "Sheet1")
     * @return Excel en bytes
     */
    public <T> byte[] generateExcel(List<T> data, List<String> columns, Function<T, List<Object>> rowMapper, String sheetName) {
        String sheet = (sheetName == null || sheetName.isEmpty()) ? "Sheet1" : sheetName;
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheetObj = workbook.createSheet(sheet);
            Row header = sheetObj.createRow(0);
            for (int i = 0; i < columns.size(); i++) {
                header.createCell(i).setCellValue(columns.get(i));
            }
            int rowIdx = 1;
            for (T item : data) {
                Row row = sheetObj.createRow(rowIdx++);
                List<Object> values = rowMapper.apply(item);
                for (int i = 0; i < values.size(); i++) {
                    Object value = values.get(i);
                    Cell cell = row.createCell(i);
                    if (value == null) {
                        cell.setBlank();
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
            for (int i = 0; i < columns.size(); i++) {
                sheetObj.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }
}