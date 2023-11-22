package com.opentext.itom.ucmdb.integration.push.repo;

import com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology;
import com.opentext.itom.ucmdb.integration.push.framework.PushResult;
import com.opentext.itom.ucmdb.integration.push.framework.PushStatus;
import com.opentext.itom.ucmdb.integration.push.framework.PushStatusEnum;
import com.opentext.itom.ucmdb.integration.push.repo.model.ClassTypeMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.TargetMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Repository
public class PushRepository {
    private static final Logger log = LoggerFactory.getLogger(PushRepository.class);

    // UCMDB classmodel configuration
    // String : CIType or StartCIType$RelationType$EndCIType
    private Map<String, ClassTypeMeta> classTypeMetaMap;

    // target table metadata
    // String: target table name
    private TargetMeta targetMeta;

    // This map correspond to the ClassMap in ClassmodelConfigRepo
    // String: CIType
    private Map<SimpleTopology, PushStatus> pushStatusMap;

    private long lastSuccessPushTimestamp = 0L;
    private long currentPushStartTimestamp = 0L;

    private BlockingQueue<SimpleTopology> pushWaitingQueue = new LinkedBlockingQueue<>();
    private PushedCICache pushedCICache = new PushedCICache();

    public PushedCICache getPushedCICache() {
        return pushedCICache;
    }


    public Map<String, ClassTypeMeta> getClassTypeMetaMap() {
        if(classTypeMetaMap == null){
            classTypeMetaMap = new HashMap<String, ClassTypeMeta>();
        }
        return classTypeMetaMap;
    }

    public long getLastSuccessPushTimestamp() {
        return lastSuccessPushTimestamp;
    }

    public void setLastSuccessPushTimestamp(long lastSuccessPushTimestamp) {
        this.lastSuccessPushTimestamp = lastSuccessPushTimestamp;
    }

    public long getCurrentPushStartTimestamp() {
        return currentPushStartTimestamp;
    }

    public void setCurrentPushStartTimestamp(long currentPushStartTimestamp) {
        this.currentPushStartTimestamp = currentPushStartTimestamp;
    }

    public void setClassTypeMetaMap(Map<String, ClassTypeMeta> classTypeMetaMap) {
        this.classTypeMetaMap = classTypeMetaMap;
    }

    public TargetMeta getTargetMeta() {
        return targetMeta;
    }

    public void setTargetMeta(TargetMeta targetMeta) {
        this.targetMeta = targetMeta;
    }

    public void pushPushQueue(SimpleTopology simpleTopology){
        try {
            pushWaitingQueue.put(simpleTopology);
        } catch (InterruptedException e) {
            log.error("[Push]Push queue error.");
            e.printStackTrace();
        }
    }
    public int getPushQueueSize(){
        return pushWaitingQueue.size();
    }
    public SimpleTopology popPushQueue(){
        try {
            return pushWaitingQueue.take();
        } catch (InterruptedException e) {
            log.error("[Push]Pop queue error.");
            e.printStackTrace();
        }
        return null;
    }

    public Map<SimpleTopology, PushStatus> getPushStatusMap() {
        if(pushStatusMap == null){
            pushStatusMap = new HashMap<>();
        }
        return pushStatusMap;
    }

    public boolean isPushFinished() {
        for(PushStatus status : getPushStatusMap().values()){
            if(PushStatusEnum.WATING_FOR_START.equals(status.getStatus()) || PushStatusEnum.RUNNING.equals(status.getStatus())){
                return false;
            }
        }
        return true;
    }

    public void updateBatchStatus(SimpleTopology simpleTopology, CIBatch ciBatch, PushResult pushResult) {
        if(simpleTopology == null || ciBatch == null || pushResult == null){
            log.error("[PushStatus]Fatal error when updating push status. Root class type: " + simpleTopology.getClassname()
            + ". PushCIBatch: " + ciBatch + ". PushResult: " + pushResult);
            return;
        }
        PushStatus pushStatus = getPushStatusMap().get(simpleTopology);
        if(pushStatus == null){
            log.error("[PushStatus]Push status missing for Class: " + simpleTopology.getClassname());
            return;
        }
        int ciCount = 0;
        int relationCount = 0;
        for(CIEntity ciEntity : ciBatch.getCiEntityMap().values()){
            String targetId = pushResult.getIdMapping().get(ciEntity.getGlobalId());
            if(targetId == null){
                log.error("[PushStatus]Unpushed CI exist, CIType: " + ciEntity.getCiType() + ". CIId: " + ciEntity.getGlobalId());
            } else{
                pushedCICache.updateCIEntity(ciEntity, targetId, getCurrentPushStartTimestamp());
                ciCount++;
            }
        }
        // process relation
        for(String pushId : pushResult.getIdMapping().keySet()){
            if(!ciBatch.getCiEntityMap().containsKey(pushId)){
                // relation id
                pushedCICache.getRelationIdMapping().put(pushId, pushResult.getIdMapping().get(pushId));
                relationCount++;
            }
        }


        pushStatus.setCurrentBatch(ciBatch.getBatchCount());
        pushStatus.setPushedCI(pushStatus.getPushedCI() + ciCount);
        pushStatus.setPushedRelation(pushStatus.getPushedRelation() + relationCount);
        pushStatus.setErrorCI(pushStatus.getErrorCI() + pushResult.getFailureList().size());

    }

    public int getCurrentPushCICount() {
        int rlt = 0;
        for(PushStatus status : getPushStatusMap().values()){
            rlt += status.getPushedCI();
        }
        return rlt;
    }

    public int getCurrentPushRelationCount() {
        int rlt = 0;
        for(PushStatus status : getPushStatusMap().values()){
            rlt += status.getPushedRelation();
        }
        return rlt;
    }

}
