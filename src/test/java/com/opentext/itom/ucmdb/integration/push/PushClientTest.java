package com.opentext.itom.ucmdb.integration.push;


import com.opentext.itom.ucmdb.integration.push.framework.target.PushClient;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIEntity;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class PushClientTest {
    @Autowired
    PushClient ucmdbPushClient;
    @Test
    void testConnection(){
        ucmdbPushClient.testConnection();
    }

    @Test
    void testInsertBatch(){
        CIBatch ciBatch = new CIBatch(1);
        CIEntity nodeCIEntity = new CIEntity("111110fc7bf29ef4a4f59e43ffc8389a", "node");
        nodeCIEntity.getAttributeMap().put("name", "testNode");
        CIEntity rsCIEntity = new CIEntity("222220fc7bf29ef4a4f59e43ffc8389a", "running_software");
        rsCIEntity.getAttributeMap().put("name", "oracle");
        rsCIEntity.getAttributeMap().put("discovered_product_name", "OracleDB");
        rsCIEntity.getAttributeMap().put("root_container", "111110fc7bf29ef4a4f59e43ffc8389a");
        CIRelation relation = new CIRelation("composition", "111110fc7bf29ef4a4f59e43ffc8389a", "222220fc7bf29ef4a4f59e43ffc8389a");

        ciBatch.getCiEntityMap().put("111110fc7bf29ef4a4f59e43ffc8389a", nodeCIEntity);
        ciBatch.getCiEntityMap().put("222220fc7bf29ef4a4f59e43ffc8389a", rsCIEntity);
        ciBatch.getParentMap().put("111110fc7bf29ef4a4f59e43ffc8389a", Stream.of(relation).collect(Collectors.toSet()));
        ciBatch.getChildrenMap().put("222220fc7bf29ef4a4f59e43ffc8389a", Stream.of(relation).collect(Collectors.toSet()));
        ucmdbPushClient.pushBatch(ciBatch);
    }
}
