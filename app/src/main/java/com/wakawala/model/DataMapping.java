package com.wakawala.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DataMapping {

    public static String likeShareCount(int count) {
        String s;

        if ((count / 1000000) >= 1) {
            s = String.format("%.1f", (float) count / 1000000);
            if (isZero(s)) {
                s = (count / 1000000) + "M";
            } else {
                s = String.format("%.1fM", (float) count / 1000000);
            }
        } else if ((count / 1000) >= 1) {
            s = String.format("%.1f", (float) count / 1000);
            if (isZero(s)) {
                s = (count / 1000) + "K";
            } else {
                s = String.format("%.1fK", (float) count / 1000);
            }
        } else {
            s = "" + count;
        }
        return s;
    }

    private static boolean isZero(String number) {
        float value = Float.parseFloat(number);
        if ((value - Math.floor(value)) == 0.0) {
            return true;
        } else {
            return false;
        }
    }

    public static int getProgress(int goal, int moneyCollected) {
        int val = (int) ((goal - moneyCollected) * 100 / (float) goal);
        return val;
    }

    public static String dayCount(String startDate, String endDate) {

        String duration = "";
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateBefore = myFormat.parse(startDate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            Integer daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
            if (daysBetween < 0) {
                duration = "Passed!";
            } else {
                duration = Integer.toString(daysBetween) + " days left";
            }
            Log.d("Tah", duration);
        } catch (Exception e) {
            e.printStackTrace();
            return duration;
        }

        return duration;
    }

    public static String getDateAgo(long start, long end) {
        Date date = new Date(end);
        Date now = new Date(start);
        long days = getDateDiff(date, now, TimeUnit.DAYS);
        if (days < 0) {
            return "Passed!";
        } else {
            return days + " days left";
        }
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static String moneyAmount(int money) {
        String s;

        if ((money / 1000000) >= 1) {
            s = (money / 1000000) + "M";

        } else if ((money / 1000) >= 1) {
            s = (money / 1000) + "K";
        } else {
            s = "" + money;
        }
        return s;
    }

    public static String moneyCollected(int money) {
        String s;

        if (((money % 1000000) == 0) && (money != 0)) {
            s = (money / 1000000) + "M";

        } else if (((money % 1000) == 0) && (money != 0)) {
            s = (money / 1000) + "K";
        } else {
            s = "" + money;
        }
        return s;
    }


}
