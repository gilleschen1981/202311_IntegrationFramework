package com.opentext.itom.ucmdb.integration.push.configuration;

import java.util.HashSet;
import java.util.Set;

public class ClassTypeConfiguration {
    private String type;
    private Set<String> attributes;

    public ClassTypeConfiguration(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Set<String> getAttributes() {
        if(attributes == null){
            attributes = new HashSet<String>();
        }
        return attributes;
    }

    public void setAttributes(Set<String> attributes) {
        this.attributes = attributes;
    }
}
