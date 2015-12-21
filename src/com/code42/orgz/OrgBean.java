package com.code42.orgz;

import java.util.List;

public interface OrgBean {
    int getId();
    int getTotalNumUsers();
    int getTotalNumFiles();
    int getTotalNumBytes();
    List<OrgBean> getChildOrgs();
}

