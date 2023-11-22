package com.opentext.itom.ucmdb.integration.push.repo.model;

import java.util.HashMap;
import java.util.Map;

public class TargetMeta {
    private long lastUpdateTimestamp;

    private Map<String, TableMeta> targetTableMetaMap;

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public Map<String, TableMeta> getTargetTableMetaMap() {
        if(targetTableMetaMap == null){
            targetTableMetaMap = new HashMap<>();
        }
        return targetTableMetaMap;
    }

    public void setTargetTableMetaMap(Map<String, TableMeta> targetTableMetaMap) {
        this.targetTableMetaMap = targetTableMetaMap;
    }
}
