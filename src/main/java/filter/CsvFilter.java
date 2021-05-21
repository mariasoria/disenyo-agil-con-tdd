package filter;

import java.util.ArrayList;
import java.util.List;

public class CsvFilter {

    public static List<String> filter(List<String> lines) {
        List<String> result = new ArrayList<>();
        result.add(lines.get(0));
        String invoice = lines.get(1);
        String[] fields = invoice.split(",");
        int grossPriceIndex = 2;
        int netPriceIndex = 3;
        int ivaFieldIndex = 4;
        int igicFieldIndex = 5;
        String grossField = fields[grossPriceIndex];
        String netField = fields[netPriceIndex];
        String ivaField = fields[ivaFieldIndex];
        String igicField = fields[igicFieldIndex];
        String decimalRegex = "\\d+(\\.\\d+)?";

        boolean taxFieldsAreMutuallyExclusive =
                (ivaField.matches(decimalRegex) || igicField.matches(decimalRegex)) &&
                (ivaField.isEmpty() || igicField.isEmpty());

        if (taxFieldsAreMutuallyExclusive && isNetFieldCorrect(grossField, netField, ivaField)) {
            result.add(lines.get(1));
        }

        return result;
    }

    private static boolean isNetFieldCorrect(String grossField, String netField, String ivaField) {
        String netValueExpected = String.valueOf(Integer.parseInt(grossField) - (Integer.parseInt(grossField) * Integer.parseInt(ivaField) / 100));
        return netField.equals(netValueExpected);
    }
}
