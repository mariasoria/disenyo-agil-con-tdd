package filter;

import java.util.ArrayList;
import java.util.List;

public class CsvFilter {

    public static List<String> filter(List<String> lines) {
        List<String> result = new ArrayList<>();
        result.add(lines.get(0));
        String invoice = lines.get(1);
        String[] fields = invoice.split(",");
        int ivaFieldIndex = 4;
        int igicFieldIndex = 5;
        boolean taxFieldsAreMutuallyExclusive = (fields[ivaFieldIndex].isEmpty() || fields[igicFieldIndex].isEmpty()) &&
                (!(fields[ivaFieldIndex].isEmpty() && fields[igicFieldIndex].isEmpty()));

        if (taxFieldsAreMutuallyExclusive) {
           result.add(lines.get(1));
        }

        return result;
    }
}
