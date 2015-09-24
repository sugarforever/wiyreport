package com.wiysoft.report.service.model.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiliyang on 9/18/15.
 */
public class GroupNode extends Node {

    private List userObjects = new ArrayList();

    public GroupNode(long id, long value, String label, String group) {
        super(id, value, label, group);
    }

    public List getUserObjects() {
        return new ArrayList(userObjects);
    }

    public void setUserObjects(List userObjects) {
        this.userObjects = userObjects == null ? new ArrayList() : new ArrayList(userObjects);
    }

    public void appendUserObject(Object userObject) {
        if (this.userObjects == null) {
            this.userObjects = new ArrayList();
        }

        this.userObjects.add(userObject);
    }
}
