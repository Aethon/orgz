package com.code42.orgz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InMemoryOrgCollection implements OrgCollection {

    private final List<OrgBean> roots;

    public InMemoryOrgCollection(List<OrgBean> roots) {
        this.roots = Collections.unmodifiableList(roots);
    }

    @Override
    public OrgBean getOrg(int orgId) {
        return getOrg(orgId, roots);
    }

    private OrgBean getOrg(int orgId, List<OrgBean> roots) {
        for (OrgBean root : roots) {
            if (root.getId() == orgId)
                return root;
            OrgBean org = getOrg(orgId, root.getChildOrgs());
            if (org != null)
                return org;
        }
        return null;
    }

    @Override
    public List<OrgBean> getOrgTree(int orgId, boolean inclusive) {
        OrgBean org = getOrg(orgId);
        if (org == null)
            return null;
        List<OrgBean> result = new ArrayList<>();
        if (inclusive) {
            result.add(org);
        }
        List<OrgBean> queue = new ArrayList<>();
        queue.add(org);
        while (!queue.isEmpty()) {
            OrgBean next = queue.remove(0);
            List<OrgBean> children = next.getChildOrgs();
            result.addAll(children);
            queue.addAll(children);
        }
        return result;
    }
}
