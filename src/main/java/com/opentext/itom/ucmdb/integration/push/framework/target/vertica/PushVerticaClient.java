package com.opentext.itom.ucmdb.integration.push.framework.target.vertica;

import com.opentext.itom.ucmdb.integration.push.framework.PushResult;
import com.opentext.itom.ucmdb.integration.push.repo.model.TableMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.TargetMeta;
import com.opentext.itom.ucmdb.integration.push.framework.target.PushClient;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PushVerticaClient implements PushClient {
    @Override
    public boolean testConnection() {
        return false;
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
        return null;
    }


}
