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

    @Test
    void excludes_lines_with_non_decimal_tax_fields() {
        String invoiceLine = "1,02/05/2019,1000,810,XYZ,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(result, List.of(headerLine));
    }

    @Test
    void exclude_lines_with_both_tax_fields_populated_even_if_non_decimal(){
        String invoiceLine = "1,02/05/2019,1000,810,XYZ,12,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(result, List.of(headerLine));
    }

    @Test
    void exclude_lines_when_net_value_is_not_correct() {
        String invoiceLine = "1,02/05/2019,1000,700,19,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(result, List.of(headerLine));
    }
}