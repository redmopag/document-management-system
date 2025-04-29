package com.redmopag.documentmanagment.ocrservice.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

public class DateExtractor {
    private static Pattern pattern =
            Pattern.compile("\\b(\\d{2}[./]\\d{2}[./]\\d{2,4})\\b");
    private static DateTimeFormatter fullYearFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static DateTimeFormatter shortYearFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

    public static List<LocalDate> findAllDates(String text) {
        List<LocalDate> dates = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String dateStr = getPreparedString(matcher);
            try {
                tryParseString(dateStr, dates);
            } catch (Exception ignored) {
            }
        }
        return dates;
    }

    private static String getPreparedString(Matcher matcher) {
        return matcher.group(1).replace("/", ".");
    }

    private static void tryParseString(String dateStr, List<LocalDate> dates) {
        if (dateStr.length() == 10) {
            dates.add(LocalDate.parse(dateStr, fullYearFormatter));
        } else {
            dates.add(LocalDate.parse(dateStr, shortYearFormatter));
        }
    }

}
