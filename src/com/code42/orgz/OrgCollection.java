package com.code42.orgz;

import java.util.List;

public interface OrgCollection {
    OrgBean getOrg(int orgId);
    List getOrgTree(int orgId, boolean inclusive);
}
