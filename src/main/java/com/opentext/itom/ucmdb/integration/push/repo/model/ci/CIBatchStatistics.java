package com.opentext.itom.ucmdb.integration.push.repo.model.ci;

public class CIBatchStatistics {
    // The CI number counted from raw gateway response
    private int rawCICount;

    // The number of CI ignored due to cache check.
    private int skippedCICount;

    // The number of CI in push bulk
    private int pushCICount;
    // The number of Relation in push bulk
    private int pushRelationCount;

    public int getRawCICount() {
        return rawCICount;
    }

    public int getSkippedCICount() {
        return skippedCICount;
    }

    public int getPushCICount() {
        return pushCICount;
    }

    public int getPushRelationCount() {
        return pushRelationCount;
    }

    public void incRawCICount() {
        this.rawCICount++;
    }

    public void incSkippedCICount() {
        this.skippedCICount++;
    }

    public void incPushCICount() {
        this.pushCICount++;
    }

    public void incPushRelationCount() {
        this.pushRelationCount++;
    }
}
