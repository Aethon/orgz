package com.code42.orgz;

import java.util.ArrayList;
import java.util.List;

public class InMemoryOrgBean implements OrgBean {

    private final int orgId;
    private Integer parentId;

    private int numBytes = 0;
    private int numFiles = 0;
    private int numUsers = 0;

    private final List<OrgBean> children = new ArrayList<>();

    private boolean frozen;

    public int getId() {
        return orgId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public InMemoryOrgBean(int orgId, Integer parentId)
    {
        this.orgId = orgId;
        this.parentId = parentId;
    }

    public void applyUser(int numFiles, int numBytes) throws IllegalStateException {
        if (frozen)
            throw new IllegalStateException("Already frozen; cannot apply users");
        numUsers++;
        this.numFiles += numFiles;
        this.numBytes += numBytes;
    }

    public void applyChild(InMemoryOrgBean child) throws IllegalStateException {
        if (frozen)
            throw new IllegalStateException("Already frozen; cannot apply children");
        children.add(child);
    }

    public boolean freeze(List<String> errors) {
        if (frozen) {
            return false;
        }
        frozen = true;
        for (OrgBean child : children) {
            if (!((InMemoryOrgBean)child).freeze(errors)) {
                errors.add(String.format("Org ID %d participates in a circular reference (counts will be incorrect)", child.getId()));
                return false;
            }
            numUsers += child.getTotalNumUsers();
            numBytes += child.getTotalNumBytes();
            numFiles += child.getTotalNumFiles();
            parentId = null;
        }
        return true;
    }


    @Override
    public int getTotalNumBytes() {
        return numBytes;
    }

    @Override
    public int getTotalNumFiles() {
        return numFiles;
    }

    @Override
    public int getTotalNumUsers() {
        return numUsers;
    }

    @Override
    public List<OrgBean> getChildOrgs() {
        return children;
    }
}
