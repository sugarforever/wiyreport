package com.wiysoft.report.common;

/**
 * Created by weiliyang on 8/27/15.
 */
public final class MathUtils {

    public static Double roundDouble(Double d, int numberOfFractions) {
        if (d == null || numberOfFractions <= 0) {
            return null;
        }

        long l = 10L;
        for (int i = 1; i < numberOfFractions; ++i) {
            l *= 10;
        }

        return Math.round(d.doubleValue() * l) / (double) l;
    }
}
