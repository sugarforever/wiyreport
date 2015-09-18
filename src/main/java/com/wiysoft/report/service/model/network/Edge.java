package com.wiysoft.report.service.model.network;

/**
 * Created by weiliyang on 9/18/15.
 */
public class Edge {

    private long from;
    private long to;
    private long value;
    private String title;

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Edge(long from, long to, long value, String title) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.title = title;
    }

    public void incrementValue(long incremental) {
        this.value += incremental;
    }
}
