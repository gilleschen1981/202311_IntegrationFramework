package com.opentext.itom.ucmdb.integration.push.repo;

import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PushedCICache {
    private static final Logger log = LoggerFactory.getLogger(PushedCICache.class);
    Map<String, String> ciIdMapping;
    Map<String, String> relationIdMapping;
    Map<String, String> idTypeMap;
    Map<String, Long> timestampMap;
    Map<String, Set<String>> typeIdMap;

    boolean isDirty = false;

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public Map<String, String> getCiIdMapping() {
        if(ciIdMapping == null){
            ciIdMapping = new HashMap<>();
        }
        return ciIdMapping;
    }

    public Map<String, String> getRelationIdMapping() {
        if(relationIdMapping == null){
            relationIdMapping = new HashMap<>();
        }
        return relationIdMapping;
    }

    public Map<String, Long> getTimestampMap() {
        if(timestampMap == null){
            timestampMap = new HashMap<>();
        }
        return timestampMap;
    }

    public Map<String, String> getIdTypeMap() {
        if(idTypeMap == null){
            idTypeMap = new HashMap<>();
        }
        return idTypeMap;
    }

    public Map<String, Set<String>> getTypeIdMap() {
        if(typeIdMap == null){
            typeIdMap = new HashMap<>();
        }
        return typeIdMap;
    }

    public void updateCIEntity(CIEntity ciEntity, String targetId, long timestamp) {
        String existingTargetId = getCiIdMapping().get(ciEntity.getGlobalId());
        if(existingTargetId != null && !existingTargetId.equals(targetId)){
            log.info("[PushCache]Id change detected. CIId: " + ciEntity.getGlobalId()
            + ". CachedTargetId: " + existingTargetId + ". newTargetId: " + targetId);
        }
        getCiIdMapping().put(ciEntity.getGlobalId(), targetId);

        String existingType = getIdTypeMap().get(ciEntity.getGlobalId());
        if(existingType != null && !existingType.equals(ciEntity.getAccurateType())){
            log.error("[PushCache]Type change detected. CIId: " + ciEntity.getGlobalId()
            + ". CachedType: " + existingType + ". newCIType: " + ciEntity.getAccurateType());
        }
        getIdTypeMap().put(ciEntity.getGlobalId(), ciEntity.getAccurateType());

        Set<String> idSet = getTypeIdMap().getOrDefault(ciEntity.getAccurateType(), new HashSet<>());
        idSet.add(ciEntity.getGlobalId());
        getTypeIdMap().put(ciEntity.getAccurateType(), idSet);

        getTimestampMap().put(ciEntity.getGlobalId(), timestamp);
        setDirty(true);
    }
}
