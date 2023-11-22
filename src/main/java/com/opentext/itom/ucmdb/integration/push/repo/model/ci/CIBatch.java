package com.opentext.itom.ucmdb.integration.push.repo.model.ci;

import com.opentext.itom.ucmdb.integration.push.framework.PushRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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

    public CIBatch(int batchCount) {
        this.batchCount = batchCount;
    }

    public int getBatchCount() {
        return batchCount;
    }

    public Map<String, CIEntity> getCiEntityMap() {
        if(ciEntityMap == null){
            ciEntityMap = new HashMap<String, CIEntity>();
        }
        return ciEntityMap;
    }

    public Map<String, Set<CIEntity>> getCiTypeMap() {
        if(ciTypeMap == null){
            ciTypeMap = new HashMap<String, Set<CIEntity>>();
        }
        return ciTypeMap;
    }

    public void addCIEntity(CIEntity ciEntity){
        getCiEntityMap().put(ciEntity.getGlobalId(), ciEntity);
        Set<CIEntity> entitySet= getCiTypeMap().getOrDefault(ciEntity.getCiType(), new HashSet<CIEntity>());
        entitySet.add(ciEntity);
        getCiTypeMap().put(ciEntity.getCiType(), entitySet);
    }

    public Map<String, Set<CIRelation>> getChildrenMap() {
        if(childrenMap == null){
            childrenMap = new HashMap<String, Set<CIRelation>>();
        }
        return childrenMap;
    }

    public Map<String, Set<CIRelation>> getParentMap() {
        if(parentMap == null){
            parentMap = new HashMap<String, Set<CIRelation>>();
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
}
