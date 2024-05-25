package com.opentext.itom.ucmdb.integration.push.framework.target.vertica.mapper;

public class MetaDataPOJO {
    String key;
    byte[] value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
