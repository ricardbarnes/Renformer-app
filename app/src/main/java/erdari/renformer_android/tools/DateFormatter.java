package erdari.renformer_android.tools;

import android.annotation.SuppressLint;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple date formatter.
 *
 * @author Ricard Pinilla Barnes
 */
public class DateFormatter {

    private static final String INPUT_DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String OUTPUT_DATE_FMT = "yyyy-MM-dd' 'HH:mm:ss";

    /**
     * Converts a Date object into a String object, with the proper date format.
     *
     * @param date The date object to convert.
     * @return The string with date format object.
     * @author Ricard Pinilla Barnes
     */
    @NotNull
    public static String dateToString(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(OUTPUT_DATE_FMT);
        return df.format(date);
    }

    /**
     * Converts a String object into a Date object.
     *
     * @param dateStr The string object to convert.
     * @return The date object.
     */
    @SuppressLint("SimpleDateFormat")
    public static Date stringToDate(String dateStr) {
        Date date = null;
        try {
            date = new SimpleDateFormat(INPUT_DATE_FMT).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
