package com.wiysoft.report.common;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by weiliyang on 9/3/15.
 */
public class DateTimeUtils {

    public static Date dateAdjust(Date date, int field, int adjust) {
        if (date == null)
            return null;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, adjust);

        return c.getTime();
    }
}
