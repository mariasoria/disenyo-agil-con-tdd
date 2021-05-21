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
        String netValueInput = fields[netPriceIndex];
        String ivaField = fields[ivaFieldIndex];
        String igicField = fields[igicFieldIndex];
        String decimalRegex = "\\d+(\\.\\d+)?";
        String ivaValueExpected = Integer.toString((Integer.parseInt(grossField) - Integer.parseInt(netValueInput)) / 10);

        boolean taxFieldsAreMutuallyExclusive =
                (ivaField.matches(decimalRegex) || igicField.matches(decimalRegex)) &&
                (ivaField.isEmpty() || igicField.isEmpty());
        boolean isIvaCorrect = ivaField.equals(ivaValueExpected);

        if (taxFieldsAreMutuallyExclusive && isIvaCorrect) {
           result.add(lines.get(1));
        }

        return result;
    }
}
