package sura.pruebalegoback.web.services;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;

class ExcelExportServiceTest {

    private final ExcelExportService service = new ExcelExportService();

    @Test
    void testGenerateExcelWithEmptyData() {
        byte[] result = service.generateExcel(List.of(), List.of("Col1", "Col2"), o -> List.of(), "Sheet1");
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void testGenerateExcelWithData() {
        record Dummy(int id, String name) {}
        List<Dummy> data = List.of(new Dummy(1, "A"), new Dummy(2, "B"));
        List<String> columns = List.of("ID", "Name");
        Function<Dummy, List<Object>> mapper = d -> List.of(d.id(), d.name());
        byte[] result = service.generateExcel(data, columns, mapper, "Sheet1");
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void testGenerateExcelWithNullValues() {
        record Dummy(Integer id, String name) {}
        List<Dummy> data = List.of(new Dummy(null, null));
        List<String> columns = List.of("ID", "Name");
        Function<Dummy, List<Object>> mapper = d -> java.util.Arrays.asList(d.id(), d.name());
        byte[] result = service.generateExcel(data, columns, mapper, "Sheet1");
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}