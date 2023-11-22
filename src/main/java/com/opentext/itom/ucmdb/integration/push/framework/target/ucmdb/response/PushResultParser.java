package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.response;

import com.opentext.itom.ucmdb.integration.push.framework.PushResult;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;

public class PushResultParser {
    public static PushResult parsePushresult(DataInStatistics dataInRlt, long startTime, long finishTime, CIBatch ciBatch) {
        PushResult rlt = new PushResult(startTime, finishTime);
        if(dataInRlt.getIdsMap() != null){
            rlt.getIdMapping().putAll(dataInRlt.getIdsMap());
        }
        if(dataInRlt.getFailureMap() != null){
            rlt.getFailureList().addAll(dataInRlt.getFailureMap().keySet());
        }
        for(String id : ciBatch.getCiEntityMap().keySet()){
            if(rlt.getIdMapping().get(id) == null){
                // unpushed CI
                rlt.getUnPushedIdSet().add(id);
            }
        }
        return rlt;
    }
}
