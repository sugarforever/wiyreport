package com.wiysoft.report.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;

/**
 * Created by weiliyang on 8/25/15.
 */
public final class CommonUtils {

    private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public static final Date parseStrToDate(String strDate, String fmt) {
        if (StringUtils.isEmpty(strDate) || StringUtils.isEmpty(fmt)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Date parsed = null;

        try {
            parsed = sdf.parse(strDate);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }

        return parsed;
    }

    public static final String parseStrFromDate(Date date, String fmt) {
        if (date == null || StringUtils.isEmpty(fmt)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        String parsed = sdf.format(date);

        return parsed;
    }

    public static final Float parseFloat(String str, Float defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(), ex);
            return defaultValue;
        }
    }

    public static final long getCRC32(String str) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        }
        CRC32 crc32 = new CRC32();
        crc32.update(str.getBytes());
        return crc32.getValue();
    }
}
