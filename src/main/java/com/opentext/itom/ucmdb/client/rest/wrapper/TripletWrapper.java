package com.opentext.itom.ucmdb.client.rest.wrapper;

public class TripletWrapper {
    private String end1Type;
    private String end2Type;
    private String linkType;
    private boolean isForward;

    public String getEnd1Type() {
        return end1Type;
    }

    public void setEnd1Type(String end1Type) {
        this.end1Type = end1Type;
    }

    public String getEnd2Type() {
        return end2Type;
    }

    public void setEnd2Type(String end2Type) {
        this.end2Type = end2Type;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public boolean isForward() {
        return isForward;
    }

    public void setForward(boolean forward) {
        isForward = forward;
    }
}
