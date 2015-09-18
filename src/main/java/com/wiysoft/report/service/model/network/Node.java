package com.wiysoft.report.service.model.network;

/**
 * Created by weiliyang on 9/18/15.
 */
public class Node {

    private long id;
    private long value;
    private String label;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Node(long id, long value, String label) {
        this.id = id;
        this.value = value;
        this.label = label;
    }

    public void incrementValue(long incremental) {
        this.value += incremental;
    }
}
