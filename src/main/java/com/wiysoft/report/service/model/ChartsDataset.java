package com.wiysoft.report.service.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiliyang on 8/26/15.
 */
public class ChartsDataset {

    private List data = new ArrayList();

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
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
