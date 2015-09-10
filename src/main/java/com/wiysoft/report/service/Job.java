package com.wiysoft.report.service;

/**
 * Created by weiliyang on 8/24/15.
 */
public interface Job {

    public void run(Object param);

    public boolean isRunning();
}
