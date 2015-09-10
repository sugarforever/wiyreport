package com.wiysoft.report.mvc.model;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.common.DateTimeUtils;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by weiliyang on 9/3/15.
 */
public class TimeRange {

    private Date startDate;
    private Date endDate;

    public TimeRange(String strDateStart, String strDateEnd, String dateFormat) {
        Date now = Calendar.getInstance().getTime();

        this.startDate = StringUtils.isEmpty(strDateStart) ? DateTimeUtils.dateAdjust(now, Calendar.DAY_OF_YEAR, -30) : CommonUtils.parseStrToDate(strDateStart, dateFormat);
        this.endDate = StringUtils.isEmpty(strDateEnd) ? now : CommonUtils.parseStrToDate(strDateEnd, dateFormat);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
