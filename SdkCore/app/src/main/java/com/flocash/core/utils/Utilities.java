package com.flocash.core.utils;

import android.support.annotation.Keep;

/**
 * Created by ${binhpd} on 8/31/2016.
 */
@Keep
public class Utilities {
    public static String convertAmount(String amount) {
        float number = Float.valueOf(amount);
        return String.format("%.2f", number);
    }
}
