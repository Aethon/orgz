package com.code42.orgz.tests;

public class OrgExpectation {
    private int id;
    private int totalNumUsers;
    private int totalNumFiles;
    private int totalNumBytes;

    public OrgExpectation(int id, int totalNumUsers, int totalNumFiles, int totalNumBytes) {
        this.id = id;
        this.totalNumUsers = totalNumUsers;
        this.totalNumFiles = totalNumFiles;
        this.totalNumBytes = totalNumBytes;
    }

    public int getId() {
        return id;
    }

    public int getTotalNumUsers() {
        return totalNumUsers;
    }

    public int getTotalNumFiles() {
        return totalNumFiles;
    }

    public int getTotalNumBytes() {
        return totalNumBytes;
    }
}
