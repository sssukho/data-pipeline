package com.sssukho.disruptorlog.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeUtils {
    static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("yyyyMMddHH0000");
    static final SimpleDateFormat HOUR_FORMAT_2 = new SimpleDateFormat("yyyyMMddHHmmss");

    static final String DATE_FORMAT = "yyyy-MM-dd 00:00:00";

    static AtomicInteger seq = new AtomicInteger(0);

    static public long getStartHourTime(long current) {
        Date beforeDate = new Date(current);
        String beforeDateStr = HOUR_FORMAT.format(beforeDate);

        Date before = null;
        try {
            before = HOUR_FORMAT_2.parse(beforeDateStr);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return before.getTime();
    }

    static public long getEndHourTime(long current) {
        Date afterDate = new Date(current);
        String afterDateStr = HOUR_FORMAT.format(afterDate);
        Date after = null;

        try {
            after = HOUR_FORMAT_2.parse(afterDateStr);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return after.getTime();
    }

    static public long getStartTimeOfDay(long inputTime) {
        Date date = new Date(inputTime);
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        String dateStr = formatter.format(date);

        long startTimeOfDay = Timestamp.valueOf(dateStr).getTime();

        return startTimeOfDay;
    }

    public static String generateUUID() {
        String first = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")); // 20150418004224123
        return String.format("%s%05d%s", first, seq.getAndIncrement() % 100000, "01");
    }

}
