package com.puja.mart.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Utility {
    public static int getFiveDigitRandomNumberString() {
        return new Random().nextInt(99999);
    }

    public static String getOrderId() {
        String dt = String.valueOf(new SimpleDateFormat("yyyyddmm").format(new Date()));
        Calendar instance = Calendar.getInstance();
        String id = dt + String.valueOf(new SimpleDateFormat("HHmmssSS").format(new Date()));
        System.out.println(id);
        return id;
    }
}
