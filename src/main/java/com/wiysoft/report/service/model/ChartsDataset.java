package com.wiysoft.report.service.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiliyang on 8/26/15.
 */
public class ChartsDataset {

    private List data = new ArrayList();
    private boolean y2axis;
    private String label;
    private String picture;

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public boolean isY2axis() {
        return y2axis;
    }

    public void setY2axis(boolean y2axis) {
        this.y2axis = y2axis;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(final String picture) {
        this.picture = picture;
    }

    public void appendData(Object o) {
        if (o != null) {
            if (data == null) {
                data = new ArrayList();
            }

            data.add(o);
        }
    }
}
