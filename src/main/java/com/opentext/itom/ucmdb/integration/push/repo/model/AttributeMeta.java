package com.opentext.itom.ucmdb.integration.push.repo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeMeta {
    private static final Logger log = LoggerFactory.getLogger(AttributeMeta.class);
    private String attrName;
    private AttributeTypeEnum attrType;
    private int attrSize;

    public AttributeMeta(String attrName, AttributeTypeEnum attrType, int attrSize) {
        if(attrType == null){
            log.debug("[CMDB]Attribute type can't be null, name = " + attrName);
        }
        this.attrName = attrName;
        this.attrType = attrType;
        this.attrSize = attrSize;
    }

    public String getAttrName() {
        return attrName;
    }

    public AttributeTypeEnum getAttrType() {
        return attrType;
    }

    public int getAttrSize() {
        return attrSize;
    }

}
