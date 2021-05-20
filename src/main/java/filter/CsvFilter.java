package filter;

import java.util.ArrayList;
import java.util.List;

public class CsvFilter {

    public static List<String> filter(List<String> lines) {
        List<String> result = new ArrayList<>();
        result.add(lines.get(0));

        String invoice = lines.get(1);
        String[] fields = invoice.split(",");

        if ((fields[4].isEmpty() || fields[5].isEmpty()) &&
                (!(fields[4].isEmpty() && fields[5].isEmpty()))) {
           result.add(lines.get(1));
        }

        return result;
    }
}
