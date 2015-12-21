package com.code42.orgz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InMemoryOrgCollection implements IOrgCollection {

    private final List<IOrgBean> roots;

    public InMemoryOrgCollection(List<IOrgBean> roots) {
        this.roots = Collections.unmodifiableList(roots);
    }

    @Override
    public IOrgBean getOrg(int orgId) {
        return getOrg(orgId, roots);
    }

    private IOrgBean getOrg(int orgId, List<IOrgBean> roots) {
        for (IOrgBean root : roots) {
            if (root.getId() == orgId)
                return root;
            IOrgBean org = getOrg(orgId, root.getChildOrgs());
            if (org != null)
                return org;
        }
        return null;
    }

    @Override
    public List<IOrgBean> getOrgTree(int orgId, boolean inclusive) {
        IOrgBean org = getOrg(orgId);
        if (org == null)
            return null;
        List<IOrgBean> result = new ArrayList<>();
        if (inclusive) {
            result.add(org);
        }
        List<IOrgBean> queue = new ArrayList<>();
        queue.add(org);
        while (!queue.isEmpty()) {
            IOrgBean next = queue.remove(0);
            List<IOrgBean> children = next.getChildOrgs();
            result.addAll(children);
            queue.addAll(children);
        }
        return result;
    }
}
