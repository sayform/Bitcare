package bitcare.com.br.bitcare.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Rafael on 16/10/2016.
 */
public abstract class DateTimeUtils {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static DateTime toDateTime(String data) {
        return DateTimeFormat.forPattern(DATE_PATTERN)
                                .parseDateTime(data);
    }

    public static String toString(DateTime data) {
        return DateTimeFormat.forPattern(DATE_PATTERN)
                                .print(data);
    }

}
