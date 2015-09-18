package com.wiysoft.report.service;

import com.wiysoft.report.common.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by weiliyang on 8/25/15.
 */
@Component
public class BuildProductPurchaseJob implements Job {

    private final static Logger logger = LoggerFactory.getLogger(BuildProductPurchaseJob.class);

    @Autowired
    private DAOService daoService;

    private volatile boolean running = false;

    @Override
    public void run(Object param) {
        if (param != null && (param instanceof Object[])) {
            logger.debug(String.format("%s started.", BuildProductPurchaseJob.class.getCanonicalName()));
            running = true;

            Object[] params = (Object[]) param;

            if (params.length >= 3) {
                String strDateStart = (String) params[0];
                String strDateEnd = (String) params[1];
                String strDateFormat = (String) params[2];

                Date startDate = CommonUtils.parseStrToDate(strDateStart, strDateFormat);
                Date endDate = CommonUtils.parseStrToDate(strDateEnd, strDateFormat);

                daoService.buildProductPurchaseBy(startDate, endDate);
            }

            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
