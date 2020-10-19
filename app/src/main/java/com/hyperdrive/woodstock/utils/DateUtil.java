package com.hyperdrive.woodstock.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    private static String TAG = "DATE_UTIL";

    public static String DD_MM_YYYY = "dd/MM/yyyy";
    public static String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    public static String UTC_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String parseDateFromUtc(String dateToParse, String formatPattern)  {
        if(dateToParse != null) {
            try {
                SimpleDateFormat sdfparse = new SimpleDateFormat(UTC_DATE,
                        Locale.getDefault());
                sdfparse.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date date = sdfparse.parse(dateToParse);

                SimpleDateFormat sdfFormat = new SimpleDateFormat(formatPattern,
                        Locale.getDefault());
                sdfFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                return sdfFormat.format(date);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        return "";
    }

    public static String formatDateToServer(String dateToParse, String parsePattern) {
        if(!dateToParse.isEmpty()) {
            try {
                SimpleDateFormat sdfparse = new SimpleDateFormat(parsePattern,
                        Locale.getDefault());
                sdfparse.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date date = sdfparse.parse(dateToParse);

                SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault());
                sdfFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                return sdfFormat.format(date);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
                return "";
            }
        }
        return "";
    }

    public static String formatDateFromServer(String dateToParse, String formatPattern) {
        if(!dateToParse.isEmpty()) {
            try {
                SimpleDateFormat sdfparse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault());
                sdfparse.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date date = sdfparse.parse(dateToParse);

                SimpleDateFormat sdfFormat = new SimpleDateFormat(formatPattern,
                        Locale.getDefault());
                sdfFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                return sdfFormat.format(date);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
                return "";
            }
        }
        return "";
    }

    public static String formatDate(String dateToParse, String parsePattern, String formatPattern) {
        if(!dateToParse.isEmpty()) {
            try {
                SimpleDateFormat sdfparse = new SimpleDateFormat(parsePattern,
                        Locale.getDefault());
                sdfparse.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date date = sdfparse.parse(dateToParse);

                SimpleDateFormat sdfFormat = new SimpleDateFormat(formatPattern,
                        Locale.getDefault());
                sdfFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                return sdfFormat.format(date);
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
                return "";
            }
        }
        return "";
    }
}
