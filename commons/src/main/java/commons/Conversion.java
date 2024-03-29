package commons;

import java.util.Date;

public record Conversion(String from, String to, double conversionRate, Date date) {
}
