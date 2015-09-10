package com.wiysoft.report;

/**
 * Created by weiliyang on 8/24/15.
 */
public interface Constants {

    public static final String SESSION_ATTR_LOGIN_USER = "loginUser";

    // Trade Status
    public static final String STATUS_TRADE_FINISHED = "TRADE_FINISHED";
    public static final String STATUS_TRADE_CLOSED = "TRADE_CLOSED";
    public static final String STATUS_TRADE_CLOSED_BY_TAOBAO = "TRADE_CLOSED_BY_TAOBAO";

    public static final String[] FINAL_TRADE_STATUS = new String[]{STATUS_TRADE_FINISHED, STATUS_TRADE_CLOSED, STATUS_TRADE_CLOSED_BY_TAOBAO};

    public static final String[] NON_STATISTICS_TRADE_STATUS = new String[]{STATUS_TRADE_CLOSED, STATUS_TRADE_CLOSED_BY_TAOBAO};
}
