package com.wiysoft.report.service.model.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiliyang on 9/18/15.
 */
public final class Data {

    private List<Node> nodes = new ArrayList<Node>();
    private List<Edge> edges = new ArrayList<Edge>();

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Data(List<Node> nodes, List<Edge> edges) {
        if (nodes != null)
            this.nodes = new ArrayList<Node>(nodes);
        if (edges != null)
            this.edges = new ArrayList<Edge>(edges);
    }
}
