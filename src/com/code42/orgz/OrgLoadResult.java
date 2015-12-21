package com.code42.orgz;

import java.util.Collections;
import java.util.List;

public final class OrgLoadResult {
    private final List<String> errors;
    private final IOrgCollection collection;
    private final List<IOrgBean> roots;

    public OrgLoadResult(IOrgCollection collection, List<IOrgBean> roots, List<String> errors) {
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

    public IOrgCollection getCollection() {
        return collection;
    }

    public List<IOrgBean> getRoots() {
        return roots;
    }
}
