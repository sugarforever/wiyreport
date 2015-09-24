package com.wiysoft.report.service.model.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by weiliyang on 9/18/15.
 */
public class Node {

    protected long id;
    protected long value;
    protected String label;
    protected String picture;
    protected String group;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Node(long id, long value, String label, String group) {
        this.id = id;
        this.value = value;
        this.label = label;
        this.group = group;
    }

    public void incrementValue(long incremental) {
        this.value += incremental;
    }
}
