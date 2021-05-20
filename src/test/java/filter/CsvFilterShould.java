package filter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvFilterShould {

    String headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente,NIF_cliente";

    @Test
    void allow_for_correct_lines_only() {
        String invoiceLine = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(result, List.of(headerLine, invoiceLine));
    }

    @Test
    void exclude_lines_with_both_tax_fields_populated_as_they_are_exclusive() {
        String invoiceLine = "1,02/05/2019,1000,810,19,8,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(result, List.of(headerLine));
    }

    @Test
    void exclude_lines_with_both_tax_fields_empty_as_one_is_required() {
        String invoiceLine = "1,02/05/2019,1000,810,,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(result, List.of(headerLine));
    }

}