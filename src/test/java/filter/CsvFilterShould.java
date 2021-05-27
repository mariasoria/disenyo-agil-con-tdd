package filter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvFilterShould {

    String headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente,NIF_cliente";

    @Test
    void allow_for_correct_lines_only() {
        String invoiceLine = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals( List.of(headerLine, invoiceLine), result);
    }

    @Test
    void exclude_lines_with_both_tax_fields_populated_as_they_are_exclusive() {
        String invoiceLine = "1,02/05/2019,1000,810,19,8,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(List.of(headerLine), result);
    }

    @Test
    void exclude_lines_with_both_tax_fields_empty_as_one_is_required() {
        String invoiceLine = "1,02/05/2019,1000,810,,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(List.of(headerLine), result);
    }

    @Test
    void excludes_lines_with_non_decimal_tax_fields() {
        String invoiceLine = "1,02/05/2019,1000,810,XYZ,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(List.of(headerLine), result);
    }

    @Test
    void exclude_lines_with_both_tax_fields_populated_even_if_non_decimal(){
        String invoiceLine = "1,02/05/2019,1000,810,XYZ,12,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(List.of(headerLine), result);
    }

    @Test
    void exclude_lines_when_net_value_is_not_correct() {
        String invoiceLine = "1,02/05/2019,1000,700,19,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(List.of(headerLine), result);
    }

    @Test
    void exclude_lines_with_cif_and_nif_fields_populated_as_they_are_exclusive() {
        String invoiceLine = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,B12344567";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(List.of(headerLine), result);
    }

    @Test
    void one_line_file_is_not_valid() {
        String invoiceLine = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(invoiceLine));

        assertEquals( List.of(), result);
    }

    @Test
    void an_empty_file_is_not_valid() {
        List<String> result = CsvFilter.filter(new ArrayList<>());

        assertEquals( List.of(), result);
    }

    @Test
    void exclude_repeated_lines_with_same_invoice_number_simple() {
        String invoiceLine1 = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,";
        String invoiceLine2 = "1,03/05/2019,1000,810,19,,Macbook Pro,,B76543321";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine1, invoiceLine2));

        assertEquals(List.of(headerLine), result);
    }

    @Test
    void exclude_repetead_lines_with_same_invoice_number_extended() {
        String invoiceLine1 = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,";
        String invoiceLine2 = "2,05/05/2019,1000,810,19,,LENOVO Thinkpad,B77818711,";
        String invoiceLine3 = "1,03/05/2019,1000,810,19,,Macbook Pro,B77818711,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine1, invoiceLine2, invoiceLine3));

        assertEquals( List.of(headerLine, invoiceLine2), result);
    }

}