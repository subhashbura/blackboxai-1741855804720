package com.provizit.qrscanner.Services;

import android.util.Log;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeUtils {

    public static boolean isTodayValid(double startTimestamp, double endTimestamp, String type) {
        long nowMillis =  utcToLocal(System.currentTimeMillis()); // UTC time in millis
        long startMillis = utcToLocal1((long) (startTimestamp * 1000)); // also UTC
        long endMillis = utcToLocal1((long) (endTimestamp * 1000));     // also UTC

        Log.d("TimeCheck", "Now (UTC): " + nowMillis);

        if ("2".equals(type)) {

            return nowMillis >= startMillis && nowMillis <= endMillis;
        } else {

            Calendar startCal = Calendar.getInstance();
            startCal.setTimeInMillis(startMillis);
            zeroTime(startCal);

            Calendar endCal = Calendar.getInstance();
            endCal.setTimeInMillis(endMillis);
            zeroTime(endCal);

            Calendar todayCal = Calendar.getInstance();
            todayCal.setTimeInMillis(nowMillis);
            zeroTime(todayCal);

            return !todayCal.before(startCal) && !todayCal.after(endCal);
        }
    }

    private static void zeroTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
    public static long utcToLocal(long utcMillis) {
        TimeZone tz = TimeZone.getDefault();
        return utcMillis + tz.getOffset(utcMillis);
    }
    public static long utcToLocal1(long utcMillis) {
        TimeZone tz = TimeZone.getDefault();
        return utcMillis - tz.getOffset(utcMillis);
    }
}
