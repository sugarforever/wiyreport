package com.wiysoft.report.service.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiliyang on 8/26/15.
 */
public class ChartsData {


    private List<String> labels = new ArrayList<String>();
    private List<ChartsDataset> datasets = new ArrayList<ChartsDataset>();

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<ChartsDataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<ChartsDataset> datasets) {
        this.datasets = datasets;
    }

    public void appendLabel(String label) {
        if (label != null) {
            if (labels == null) {
                labels = new ArrayList<String>();
            }

            labels.add(label);
        }
    }

    public void appendDataset(ChartsDataset dataset) {
        if (dataset != null) {
            if (datasets == null) {
                datasets = new ArrayList<ChartsDataset>();
            }

            datasets.add(dataset);
        }
    }
}
