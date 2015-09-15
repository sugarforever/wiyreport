package com.wiysoft.report.service.model;

/**
 * Created by weiliyang on 9/13/15.
 */
public class VisData {

    private final int id;
    private final String content;
    private final String start;
    private final String end;
    private final String type;

    public VisData(int id, String content, String start, String end, String type) {
        this.id = id;
        this.content = content;
        this.start = start;
        this.end = end;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
