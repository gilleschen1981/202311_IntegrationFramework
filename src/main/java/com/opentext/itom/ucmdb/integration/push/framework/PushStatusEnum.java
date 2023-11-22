package com.opentext.itom.ucmdb.integration.push.framework;

public enum PushStatusEnum{

    WATING_FOR_START("Waiting"),
    RUNNING("Running"),
    FINISHED("Finished"),
    ERROR("Error");


    private String text;

    PushStatusEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static PushStatusEnum fromString(String text) {
        if (text != null) {
            for (PushStatusEnum b : PushStatusEnum.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
