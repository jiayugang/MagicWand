package com.android.magic.wand.utils;

import android.os.Build;

import java.util.Locale;

public class Utils {

    private static boolean IS_O = Build.VERSION.SDK_INT >= 26;
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = IS_O ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (IS_O ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + "";
        return String.format(Locale.US, "%.2f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static double humanReadableByteNumber(long bytes, boolean si) {
        int unit = IS_O ? 1000 : 1024;
        if (bytes < unit) return bytes;
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        return bytes / Math.pow(unit, exp);
    }

    public static String humanReadableByteUnit(long bytes, boolean si) {
        int unit = IS_O ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (IS_O ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + "B";
        return pre;
    }
}
