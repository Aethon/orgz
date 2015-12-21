package com.code42.orgz;

import java.util.List;

public interface IOrgCollection {
    IOrgBean getOrg(int orgId);
    List getOrgTree(int orgId, boolean inclusive);
}
