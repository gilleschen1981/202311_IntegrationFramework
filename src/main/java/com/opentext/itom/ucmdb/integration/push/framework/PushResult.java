package com.opentext.itom.ucmdb.integration.push.framework;

import java.util.*;

public class PushResult {
    // <ucmdb globalid, targetid>
    private Map<String, String> idMapping;
    private List<String> failureList;
    private Set<String> unPushedIdSet;
    private final long startTime;
    private long finishTime;

    public PushResult(long startTime, long finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
    public PushResult(long startTime) {
        this.startTime = startTime;
    }

    public Map<String, String> getIdMapping() {
        if(idMapping == null){
            idMapping = new HashMap<>();
        }
        return idMapping;
    }

    public List<String> getFailureList() {
        if(failureList == null){
            failureList = new ArrayList<>();
        }
        return failureList;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public Set<String> getUnPushedIdSet() {
        if(unPushedIdSet == null){
            unPushedIdSet = new HashSet<>();
        }
        return unPushedIdSet;
    }
}
