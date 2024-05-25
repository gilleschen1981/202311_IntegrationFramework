package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb;

import com.opentext.itom.ucmdb.integration.push.framework.PushResult;
import com.opentext.itom.ucmdb.integration.push.framework.target.PushClient;
import com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.payload.UCMDBRestPayloadConverter;
import com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.response.DataInStatistics;
import com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.response.PushResultParser;
import com.opentext.itom.ucmdb.integration.push.repo.model.TableMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.TargetMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(
        value = "push.target.type",
        havingValue = "UCMDB",
        matchIfMissing = true
)
public class PushUCMDBClient implements PushClient {
    private static final Logger log = LoggerFactory.getLogger(PushUCMDBClient.class);
    @Autowired
    UCMDBRestClient ucmdbRestClient;

    @Override
    public boolean testConnection() {
        boolean rlt = false;
        try {
            ucmdbRestClient.authenticate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if(ucmdbRestClient.getToken() != null){
            rlt = true;
        }
        return rlt;
    }

    @Override
    public TargetMeta loadTargetMeta() {
        return null;
    }

    @Override
    public TargetMeta initTargetSystem(Map<String, TableMeta> tableMetaMap) {
        return null;
    }

    @Override
    public PushResult pushBatch(CIBatch ciBatch) {
        PushResult rlt = null;
        try {
            long startTime = System.currentTimeMillis();
            ucmdbRestClient.authenticate();
            DataInStatistics dataInRlt = ucmdbRestClient.batchDatain(UCMDBRestPayloadConverter.convertCIBatch2Payload(ciBatch));
            long finishTime = System.currentTimeMillis();
            rlt = PushResultParser.parsePushresult(dataInRlt, startTime, finishTime, ciBatch);
            if(rlt != null){
                log.info("[PushRunner]Push result received. Success: " + rlt.getIdMapping().size() + ". Failure: " + rlt.getFailureList().size());
                log.debug("[PushRunner]Unpushed CI list: \n");
                for(String id : rlt.getUnPushedIdSet()){
                    CIEntity ciEntity = ciBatch.getCiEntityMap().get(id);
                    log.debug(id + "\t" + ciEntity.getAccurateType() + "\t");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }

        return rlt;
    }

    @Override
    public void updateFinishTime(long finishTime) {

    }


}
