package com.code42.orgz;

import java.util.List;

public interface IOrgBean {
    int getId();
    int getTotalNumUsers();
    int getTotalNumFiles();
    int getTotalNumBytes();
    List<IOrgBean> getChildOrgs();
}
