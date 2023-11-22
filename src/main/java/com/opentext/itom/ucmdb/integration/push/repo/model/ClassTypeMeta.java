package com.opentext.itom.ucmdb.integration.push.repo.model;

import java.util.ArrayList;
import java.util.List;

public class ClassTypeMeta {
    private final String className;
    private boolean isRelation = false;
    private String End1Class = null;
    private String End2Class = null;
    private List<AttributeMeta> attributeMetaList;

    public ClassTypeMeta(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public boolean isRelation() {
        return isRelation;
    }

    public void setRelation(boolean relation) {
        isRelation = relation;
    }

    public String getEnd1Class() {
        return End1Class;
    }

    public void setEnd1Class(String end1Class) {
        End1Class = end1Class;
    }

    public String getEnd2Class() {
        return End2Class;
    }

    public void setEnd2Class(String end2Class) {
        End2Class = end2Class;
    }

    public List<AttributeMeta> getAttributeMetaList() {
        if(attributeMetaList == null){
            attributeMetaList = new ArrayList<>();
        }
        return attributeMetaList;
    }

    public AttributeTypeEnum getAttrTypeFromName(String attrName) {
        for(AttributeMeta attr : getAttributeMetaList()){
            if(attrName.equals(attr.getAttrName())){
                return attr.getAttrType();
            }
        }
        return null;
    }
}
