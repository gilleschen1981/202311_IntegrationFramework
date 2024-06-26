package com.opentext.itom.ucmdb.integration.push.framework.target;

import com.opentext.itom.ucmdb.integration.push.framework.PushResult;
import com.opentext.itom.ucmdb.integration.push.repo.model.TableMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.TargetMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;

import java.util.Map;

public interface PushClient {
    boolean testConnection();

    TargetMeta loadTargetMeta();

    TargetMeta initTargetSystem(Map<String, TableMeta> tableMetaMap);

    PushResult pushBatch(CIBatch ciBatch);

    void updateFinishTime(long finishTime);
}
