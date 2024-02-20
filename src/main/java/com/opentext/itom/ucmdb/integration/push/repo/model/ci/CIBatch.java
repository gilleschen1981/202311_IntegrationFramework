package com.opentext.itom.ucmdb.integration.push.repo.model.ci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CIBatch {
    private static final Logger log = LoggerFactory.getLogger(CIBatch.class);
    // <Globalid, CIEntity>
    // This map contains all CIs, no relationship
    private Map<String, CIEntity> ciEntityMap;
    private Map<String, Set<CIEntity>> ciTypeMap;

    // <globalid, set of CIRelation with end1id == globalid>
    private Map<String, Set<CIRelation>> childrenMap;

    // <globalid, set of CIRelation with end2id == globalid>
    private Map<String, Set<CIRelation>> parentMap;
    int batchCount;
    CIBatchStatistics batchStatistics;

    public CIBatch(int batchCount) {
        this.batchCount = batchCount;
        batchStatistics = new CIBatchStatistics();
    }

    public int getBatchCount() {
        return batchCount;
    }

    public CIBatchStatistics getBatchStatistics() {
        return batchStatistics;
    }

    public Map<String, CIEntity> getCiEntityMap() {
        if(ciEntityMap == null){
            ciEntityMap = new HashMap<>();
        }
        return ciEntityMap;
    }

    public Map<String, Set<CIEntity>> getCiTypeMap() {
        if(ciTypeMap == null){
            ciTypeMap = new HashMap<>();
        }
        return ciTypeMap;
    }

    public void addCIEntity(CIEntity ciEntity){
        getCiEntityMap().put(ciEntity.getGlobalId(), ciEntity);
        Set<CIEntity> entitySet= getCiTypeMap().getOrDefault(ciEntity.getCiType(), new HashSet<>());
        entitySet.add(ciEntity);
        getCiTypeMap().put(ciEntity.getCiType(), entitySet);
        getBatchStatistics().incPushCICount();
    }

    public Map<String, Set<CIRelation>> getChildrenMap() {
        if(childrenMap == null){
            childrenMap = new HashMap<>();
        }
        return childrenMap;
    }

    public Map<String, Set<CIRelation>> getParentMap() {
        if(parentMap == null){
            parentMap = new HashMap<>();
        }
        return parentMap;
    }

    public String getStatisticsString() {
        StringBuilder rlt = new StringBuilder("[");
        for(String ciType: getCiTypeMap().keySet()){
            rlt.append(ciType).append("-").append(getCiTypeMap().get(ciType) == null ? 0 : getCiTypeMap().get(ciType).size());
            rlt.append(";");
        }
        rlt.append("]");
        return rlt.toString();
    }

    public void integrityCheck() {
        for(Set<CIRelation> relationSet : getParentMap().values()){
            if(relationSet != null){
                for(CIRelation relation: relationSet){
                    if(!getCiEntityMap().containsKey(relation.getEnd1Id())){
                        log.debug("[CIBatchIntegrity]End1 id missing for relation: " + relation.getGlobalId());
                    }
                    if(!getCiEntityMap().containsKey(relation.getEnd2Id())){
                        log.debug("[CIBatchIntegrity]End2 id missing for relation: " + relation.getGlobalId());
                    }
                }
            }
        }
        for(Set<CIRelation> relationSet : getChildrenMap().values()){
            if(relationSet != null){
                for(CIRelation relation: relationSet){
                    if(!getCiEntityMap().containsKey(relation.getEnd1Id())){
                        log.debug("[CIBatchIntegrity]End1 id missing for relation: " + relation.getGlobalId());
                    }
                    if(!getCiEntityMap().containsKey(relation.getEnd2Id())){
                        log.debug("[CIBatchIntegrity]End2 id missing for relation: " + relation.getGlobalId());
                    }
                }
            }
        }
    }

    public void addRelation(CIRelation relation) {
        Set<CIRelation> pRelationSet = getParentMap().getOrDefault(relation.getEnd1Id(), new HashSet<>());
        pRelationSet.add(relation);
        getParentMap().put(relation.getEnd1Id(), pRelationSet);
        Set<CIRelation> cRelationSet = getChildrenMap().getOrDefault(relation.getEnd2Id(), new HashSet<>());
        cRelationSet.add(relation);
        getChildrenMap().put(relation.getEnd2Id(), cRelationSet);
        getBatchStatistics().incPushRelationCount();
    }
}
