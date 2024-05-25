package com.opentext.itom.ucmdb.integration.push.framework.target.vertica;

import com.opentext.itom.ucmdb.integration.push.framework.PushResult;
import com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.UCMDBRestClient;
import com.opentext.itom.ucmdb.integration.push.repo.model.TableMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.TargetMeta;
import com.opentext.itom.ucmdb.integration.push.framework.target.PushClient;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Target;
import java.util.Map;

@Component
@ConditionalOnProperty(
        value = "push.target.type",
        havingValue = "Vertica"
)
public class PushVerticaClient implements PushClient {
    private static final Logger log = LoggerFactory.getLogger(PushVerticaClient.class);
    @Autowired
    VerticaConnector verticaConnector;

    @Override
    public boolean testConnection() {
        log.info("[Vertica]Start test db connection.");
        boolean connected = verticaConnector.testConnection();
        log.info("[Vertica]Test connection result: " + connected);
        return connected;
    }

    @Override
    public TargetMeta loadTargetMeta() {
        log.info("[Vertica]Start loading metadata stored in vertica.");
        TargetMeta rlt = verticaConnector.loadMetadata();
        log.info("[Vertica]Meta data loaded");
        return rlt;
    }

    @Override
    public TargetMeta initTargetSystem(Map<String, TableMeta> tableMetaMap) {
        log.info("[Vertica]Start initializing db schema...");
        TargetMeta rlt = verticaConnector.initTargetSystem(tableMetaMap);
        log.info("[Vertica]DB schema reset." + rlt!=null?"successfully":"failed");
        return rlt;
    }

    @Override
    public PushResult pushBatch(CIBatch ciBatch) {
        log.info("[Vertica]Start pushing CI batch to Vertica...");
        PushResult rlt = verticaConnector.batchInsert(ciBatch);
        log.info("[Vertica]CI batch pushed.");
        return rlt;
    }

    @Override
    public void updateFinishTime(long finishTime) {
        log.info("[Vertica]Push finished, set finish timestamp in vertica");
        verticaConnector.updateFinishTime(finishTime);
        log.info("[Vertica]Finish time updated");
    }


}
