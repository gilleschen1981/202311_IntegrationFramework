package com.opentext.itom.ucmdb.integration.push.repo.model;

public enum AttributeTypeEnum {
    ATTR_TYPE_STRING("STRING"),
    ATTR_TYPE_BOOLEAN("BOOLEAN"),
    ATTR_TYPE_DATE("DATE"),
    ATTR_TYPE_STRINGLIST("STRING_LIST"),
    ATTR_TYPE_LIST("LIST"),
    ATTR_TYPE_ENUM("ENUM"),
    ATTR_TYPE_INTEGER("INTEGER");

    private final String text;

    AttributeTypeEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static AttributeTypeEnum fromString(String text) {
        if (text != null) {
            for (AttributeTypeEnum b : AttributeTypeEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
