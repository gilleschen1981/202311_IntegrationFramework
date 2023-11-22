package com.opentext.itom.ucmdb.integration.push.framework;

public class PushStatus {
    private int currentBatch = 0;
    private PushStatusEnum status = PushStatusEnum.WATING_FOR_START;

    private int pushedCI = 0;

    private int pushedRelation = 0;
    private int errorCI = 0;

    public int getCurrentBatch() {
        return currentBatch;
    }

    public void setCurrentBatch(int currentBatch) {
        this.currentBatch = currentBatch;
    }

    public PushStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PushStatusEnum status) {
        this.status = status;
    }

    public int getPushedCI() {
        return pushedCI;
    }

    public void setPushedCI(int pushedCI) {
        this.pushedCI = pushedCI;
    }

    public int getPushedRelation() {
        return pushedRelation;
    }

    public void setPushedRelation(int pushedRelation) {
        this.pushedRelation = pushedRelation;
    }

    public int getErrorCI() {
        return errorCI;
    }

    public void setErrorCI(int errorCI) {
        this.errorCI = errorCI;
    }
}
