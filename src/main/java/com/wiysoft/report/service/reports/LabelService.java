package com.wiysoft.report.service.reports;

import com.wiysoft.report.common.CommonUtils;
import com.wiysoft.report.common.DateTimeUtils;
import com.wiysoft.report.entity.TimeRangeEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * Created by weiliyang on 9/30/15.
 */
@Service
public class LabelService {

    public String getLabel(TimeRangeEntity timeRangeEntity, String dateFormat) {
        if (timeRangeEntity == null) {
            return "";
        }

        String fmt = StringUtils.isEmpty(dateFormat) ? "yyyy-MM-dd HH:mm:ss" : dateFormat;
        String strDateStart = CommonUtils.parseStrFromDate(timeRangeEntity.getStartDate(), fmt);
        String strDateEnd = CommonUtils.parseStrFromDate(timeRangeEntity.getEndDate(), fmt);

        return String.format("%s ~ %s", strDateStart, strDateEnd);
    }
}
