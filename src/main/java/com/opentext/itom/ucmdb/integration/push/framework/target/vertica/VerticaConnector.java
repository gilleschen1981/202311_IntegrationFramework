package com.opentext.itom.ucmdb.integration.push.framework.target.vertica;

import com.opentext.itom.ucmdb.integration.push.framework.PushResult;
import com.opentext.itom.ucmdb.integration.push.repo.model.TableMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.TargetMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;

import java.util.Map;

public interface VerticaConnector {
    boolean testConnection();

    TargetMeta loadMetadata();

    TargetMeta initTargetSystem(Map<String, TableMeta> tableMetaMap);

    PushResult batchInsert(CIBatch ciBatch);

    void updateFinishTime(long finishTime);
}
