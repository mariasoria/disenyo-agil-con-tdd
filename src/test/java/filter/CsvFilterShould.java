package filter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvFilterShould {

    @Test
    void dummyTest() {
        assertEquals(4, 2+2);
    }

    @Test
    void allow_for_correct_lines_only() {
        String headerLine = "Num_factura, Fecha, Bruto, Neto, IVA, IGIC, Concepto, CIF_cliente,NIF_cliente";
        String invoiceLine = "1,02/05/2019,1000,810,19,,ACER Laptop,B76430134,";
        List<String> result = CsvFilter.filter(List.of(headerLine, invoiceLine));

        assertEquals(result, List.of(headerLine, invoiceLine));
    }

}