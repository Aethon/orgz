package com.code42.orgz;

import java.util.Collections;
import java.util.List;

public final class OrgLoadResult {
    private final List<String> errors;
    private final OrgCollection collection;
    private final List<OrgBean> roots;

    public OrgLoadResult(OrgCollection collection, List<OrgBean> roots, List<String> errors) {
        this.collection = collection;
        this.roots = Collections.unmodifiableList(roots);
        this.errors = Collections.unmodifiableList(errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }

    public OrgCollection getCollection() {
        return collection;
    }

    public List<OrgBean> getRoots() {
        return roots;
    }
}
