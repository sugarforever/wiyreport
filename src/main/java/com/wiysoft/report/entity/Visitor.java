package com.wiysoft.report.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * Created by weiliyang on 8/24/15.
 */
@Entity
@Table(name = "t_visitor")
public class Visitor {

    @Id
    private long visitorId;

    @Column
    private String visitorNick;
    @Column
    private String sessionKey;
    @Column
    private String refreshToken;
    @Column
    private int w1ExpiresIn;
    @Column
    private int w2ExpiresIn;
    @Column
    private int r1ExpiresIn;
    @Column
    private int reExpiresIn;
    @Column
    private int r2ExpiresIn;
    @Column
    private int expiresIn;
    @Column
    private Date ts;

    public long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(long visitorId) {
        this.visitorId = visitorId;
    }

    public String getVisitorNick() {
        return visitorNick;
    }

    public void setVisitorNick(String visitorNick) {
        this.visitorNick = visitorNick;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getW1ExpiresIn() {
        return w1ExpiresIn;
    }

    public void setW1ExpiresIn(int w1ExpiresIn) {
        this.w1ExpiresIn = w1ExpiresIn;
    }

    public int getW2ExpiresIn() {
        return w2ExpiresIn;
    }

    public void setW2ExpiresIn(int w2ExpiresIn) {
        this.w2ExpiresIn = w2ExpiresIn;
    }

    public int getR1ExpiresIn() {
        return r1ExpiresIn;
    }

    public void setR1ExpiresIn(int r1ExpiresIn) {
        this.r1ExpiresIn = r1ExpiresIn;
    }

    public int getReExpiresIn() {
        return reExpiresIn;
    }

    public void setReExpiresIn(int reExpiresIn) {
        this.reExpiresIn = reExpiresIn;
    }

    public int getR2ExpiresIn() {
        return r2ExpiresIn;
    }

    public void setR2ExpiresIn(int r2ExpiresIn) {
        this.r2ExpiresIn = r2ExpiresIn;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public static Visitor build(Map<String, String> params) {
        if (params == null || params.size() == 0)
            return null;

        Visitor v = new Visitor();
        v.setExpiresIn(Integer.parseInt(params.get("expires_in")));
        v.setR1ExpiresIn(Integer.parseInt(params.get("r1_expires_in")));
        v.setR2ExpiresIn(Integer.parseInt(params.get("r2_expires_in")));
        v.setReExpiresIn(Integer.parseInt(params.get("re_expires_in")));
        v.setRefreshToken(params.get("refresh_token"));
        v.setTs(new Date(Long.parseLong(params.get("ts"))));
        v.setVisitorId(Integer.parseInt(params.get("visitor_id")));
        v.setVisitorNick(params.get("visitor_nick"));
        v.setW1ExpiresIn(Integer.parseInt(params.get("w1_expires_in")));
        v.setW2ExpiresIn(Integer.parseInt(params.get("w2_expires_in")));
        v.setSessionKey(params.get("sessionKey"));

        return v;
    }
}
