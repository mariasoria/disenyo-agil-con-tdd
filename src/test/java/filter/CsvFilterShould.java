package filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvFilterShould {

    String headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente,NIF_cliente";
    CsvFilter filter;
    FileWithOneInvoiceLineBuilder builder;
    private final List<String> emptyDataFile = List.of(headerLine);
    private final String emptyField = "";

    @BeforeEach
    public void setup() {
        filter = new CsvFilter();
        builder = new FileWithOneInvoiceLineBuilder();
    }

    @Test
    void allow_for_correct_lines_only() {
        List<String> lines = builder
                .withHeader(headerLine)
                .withConcept("a correct line with irrelevant data")
                .generateLines();

        List<String> result = filter.apply(lines);
        assertEquals(lines, result);
    }

    @Test
    void exclude_lines_with_both_tax_fields_populated_as_they_are_exclusive() {
        List<String> result = filter.apply(builder
                .withHeader(headerLine)
                .withIvaTax("19")
                .withIgicTax("8")
                .generateLines());

        assertEquals(emptyDataFile, result);
    }

    @Test
    void exclude_lines_with_both_tax_fields_empty_as_one_is_required() {
        List<String> result = filter.apply(builder
                .withHeader(headerLine)
                .withIvaTax(emptyField)
                .withIgicTax(emptyField)
                .generateLines());

        assertEquals(emptyDataFile, result);
    }

    @Test
    void excludes_lines_with_non_decimal_tax_fields() {
        List<String> result = filter.apply(builder
                .withHeader(headerLine)
                .withIvaTax("XYZ")
                .withIgicTax(emptyField)
                .generateLines());

        assertEquals(emptyDataFile, result);
    }

    @Test
    void exclude_lines_with_both_tax_fields_populated_even_if_non_decimal(){
        List<String> result = filter.apply(builder
                .withHeader(headerLine)
                .withIvaTax("XYZ")
                .withIgicTax("12")
                .generateLines());

        assertEquals(emptyDataFile, result);
    }

    @Test
    void exclude_lines_when_net_value_is_not_correct() {
        List<String> result = filter.apply(builder
                .withHeader(headerLine)
                .withNetValue("700")
                .withIvaTax("19")
                .generateLines());

        assertEquals(emptyDataFile, result);
    }

    @Test
    void exclude_lines_with_cif_and_nif_fields_populated_as_they_are_exclusive() {
        List<String> result = filter.apply(builder
                .withHeader(headerLine)
                .withCif("B76430134")
                .withNif("B12344567")
                .generateLines());

        assertEquals(emptyDataFile, result);
    }

    @Test
    void one_line_file_is_not_valid() {
        String invoiceLine = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,";
        List<String> result = filter.apply(List.of(invoiceLine));

        assertEquals( List.of(), result);
    }

    @Test
    void an_empty_file_is_not_valid() {
        List<String> result = filter.apply(new ArrayList<>());

        assertEquals( List.of(), result);
    }

    @Test
    void exclude_repeated_lines_with_same_invoice_number_simple() {
        String invoiceLine1 = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,";
        String invoiceLine2 = "1,03/05/2019,1000,810,19,,Macbook Pro,,B76543321";
        List<String> result = filter.apply(List.of(headerLine, invoiceLine1, invoiceLine2));

        assertEquals(List.of(headerLine), result);
    }

    @Test
    void exclude_repetead_lines_with_same_invoice_number_extended() {
        String invoiceLine1 = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,";
        String invoiceLine2 = "2,05/05/2019,1000,810,19,,LENOVO Thinkpad,B77818711,";
        String invoiceLine3 = "1,03/05/2019,1000,810,19,,Macbook Pro,B77818711,";
        List<String> result = filter.apply(List.of(headerLine, invoiceLine1, invoiceLine2, invoiceLine3));

        assertEquals( List.of(headerLine, invoiceLine2), result);
    }

}