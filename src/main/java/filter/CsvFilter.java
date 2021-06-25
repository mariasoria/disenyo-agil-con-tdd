package filter;

import java.util.ArrayList;
import java.util.List;

public class CsvFilter {

    public List<String> apply(List<String> lines) {
        List<String> result = new ArrayList<>();
        boolean fileIsNotValid = lines.size() == 1;
        boolean fileIsEmpty = lines.size() == 0;
        if (fileIsNotValid || fileIsEmpty) {
            return result;
        }
        result.add(lines.get(0));
        int grossPriceIndex = 2;
        int netPriceIndex = 3;
        int ivaFieldIndex = 4;
        int igicFieldIndex = 5;
        int cifFieldIndex = 7;
        int nifFieldIndex = 8;

        for(int i=1; i < lines.size(); i++) {
            String invoice = lines.get(i);
            String[] fields = invoice.split(",");
            String grossField = fields[grossPriceIndex];
            String netField = fields[netPriceIndex];
            String ivaField = fields[ivaFieldIndex];
            String igicField = fields[igicFieldIndex];
            String cifField = fields[cifFieldIndex];
            String decimalRegex = "\\d+(\\.\\d+)?";
            String nifField = getNifField(nifFieldIndex, fields);
            boolean taxFieldsAreMutuallyExclusive =
                    (ivaField.matches(decimalRegex) || igicField.matches(decimalRegex)) &&
                    (ivaField.isEmpty() || igicField.isEmpty()) && (cifField.isEmpty() || nifField.isEmpty());

            if (taxFieldsAreMutuallyExclusive && isNetFieldCorrect(grossField, netField, ivaField)) {
                result.add(lines.get(i));
            }
        }
        if (result.size() > 2) {
            result = removeRepeatedInvoiceNumbersLines(result);
        }
        return result;
    }

    private static String getNifField(int nifFieldIndex, String[] fields) {
        String nifField;
        try {
            nifField = fields[nifFieldIndex];
        } catch(IndexOutOfBoundsException ex) {
            nifField = "";
        }
        return nifField;
    }

    private static List<String> removeRepeatedInvoiceNumbersLines(List<String> csvLinesValidated) {
        List<String> uniqueInvoiceNumberLines = new ArrayList<>();
        List<String> ocurrencesBuffer = new ArrayList<>();

        for (int fixedIndex = 0; fixedIndex < csvLinesValidated.size(); fixedIndex++) {
            String[] linesOutterFor = csvLinesValidated.get(fixedIndex).split(",");
            for (String s : csvLinesValidated) {
                String[] linesInnerFor = s.split(",");
                if (linesOutterFor[0].equals(linesInnerFor[0])) {
                    ocurrencesBuffer.add(csvLinesValidated.get(fixedIndex));
                }
            }
            if(ocurrencesBuffer.size() == 1) {
                uniqueInvoiceNumberLines.add(ocurrencesBuffer.get(0));
            }
            ocurrencesBuffer = new ArrayList<>();
        }
        return uniqueInvoiceNumberLines;
    }

    private static boolean isNetFieldCorrect(String grossField, String netField, String ivaField) {
        String netValueExpected = String.valueOf(Integer.parseInt(grossField) - (Integer.parseInt(grossField) * Integer.parseInt(ivaField) / 100));
        return netField.equals(netValueExpected);
    }
}
