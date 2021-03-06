package com.wiysoft.report.scheduled;

import com.wiysoft.report.entity.Visitor;
import com.wiysoft.report.repository.VisitorRepository;
import com.wiysoft.report.service.RefreshTokenJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by weiliyang on 7/24/15.
 */
@Component
@Configurable
@EnableScheduling
public class ScheduledRefreshTokenTask {

    private final static Logger logger = LoggerFactory.getLogger(ScheduledRefreshTokenTask.class);

    @Autowired
    private RefreshTokenJob refreshTokenJob;
    @Autowired
    private VisitorRepository visitorRepository;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 50, 1, TimeUnit.HOURS, new LinkedBlockingDeque<Runnable>());

    @Scheduled(cron = "${wiyreport.application.refreshTokenJobExecutionCron}")
    public void execute() {
        if (refreshTokenJob.isRunning()) {
            logger.debug(refreshTokenJob.getClass().getCanonicalName() + " is still running.");
            return;
        } else {
            logger.debug("Job is about to start execution.");
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Pageable pageable = new PageRequest(0, 1000);
                while (true) {
                    Page<Visitor> visitors = visitorRepository.findAll(pageable);
                    for (Visitor visitor : visitors) {
                        refreshTokenJob.run(visitor);
                    }

                    if (!visitors.hasNext()) {
                        break;
                    } else {
                        pageable = pageable.next();
                    }
                }
            }
        });
    }
}
