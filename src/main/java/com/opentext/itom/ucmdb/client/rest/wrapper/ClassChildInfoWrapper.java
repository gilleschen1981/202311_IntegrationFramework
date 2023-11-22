package com.opentext.itom.ucmdb.client.rest.wrapper;

import java.util.ArrayList;
import java.util.List;

public class ClassChildInfoWrapper {
    public final String name;
    public final String displayName;
    private List<ClassChildInfoWrapper> children = new ArrayList<>();

    public ClassChildInfoWrapper(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public List<ClassChildInfoWrapper> getChildren() {
        return children;
    }

    public void setChildren(List<ClassChildInfoWrapper> children) {
        this.children = children;
    }
}
