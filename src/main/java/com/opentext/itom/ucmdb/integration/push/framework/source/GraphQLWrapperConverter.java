package com.opentext.itom.ucmdb.integration.push.framework.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.opentext.itom.ucmdb.client.graphql.resultwrapper.ResponseWrapper;
import com.opentext.itom.ucmdb.integration.push.configuration.ClassNameMappingUtil;
import com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology;
import com.opentext.itom.ucmdb.integration.push.repo.PushRepository;
import com.opentext.itom.ucmdb.integration.push.repo.model.AttributeMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIEntity;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIRelation;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GraphQLWrapperConverter {
    private static final Logger log = LoggerFactory.getLogger(GraphQLWrapperConverter.class);
    public static final String PAYLOAD_FIELD_GLOBAL_ID = "global_id";
    public static final String PAYLOAD_FIELD_CMDB_ID = "cmdb_id";
    public static final String RESPONSE_FIELD_NULL = "null";

    static ClassNameMappingUtil classNameMappingUtil;
    static PushRepository pushRepository;
    @Autowired
    ClassNameMappingUtil autoClassNameMappingUtil;
    @Autowired
    PushRepository autoPushRepository;

    @PostConstruct
    public void init(){
        classNameMappingUtil = this.autoClassNameMappingUtil;
        pushRepository = this.autoPushRepository;
    }
    public static CIBatch convertWrapper2CIBatch(ResponseWrapper resultWrapper, SimpleTopology simpleTopology, int batchCount, long timestamp) {
        if(resultWrapper == null){
            return null;
        }
        CIBatch rlt = new CIBatch(batchCount);
        for(JsonNode jsonNode : resultWrapper.getData().get(classNameMappingUtil.getGraphQLRootClassName(simpleTopology.getClassname()))){
            loopJson(jsonNode, simpleTopology, null, null, rlt, timestamp);
        }
        return rlt;
    }

    // return true: There are CI added
    // return false: No CI added.
    private static boolean loopJson(JsonNode jsonNode, SimpleTopology simpleTopology, String parentId, String parentRelationName, CIBatch rlt, long timestamp) {
        boolean isCIAdded = false;
        CIEntity currentEntity = buildCIFromJsonNode(jsonNode, simpleTopology);
        if(currentEntity == null){
            return false;
        }
        rlt.getBatchStatistics().incRawCICount();
        // first process all chidren ci
        if(simpleTopology.getRelated() != null){
            for(String relationname : simpleTopology.getRelated().keySet()){
                for(SimpleTopology child : simpleTopology.getRelated().get(relationname)){
                    for(JsonNode childJson : jsonNode.get(classNameMappingUtil.getGraphQLInnerClassName(child.getClassname()))){
                        if(childJson == null || childJson.size() <= 0){
                            log.debug("[UCMDB]The CI has no subelement, CI id:  " + currentEntity.getGlobalId() + ". SubElementType: " + child.getClassname() );
                        } else{
                           if(loopJson(childJson, child, currentEntity.getGlobalId(), relationname, rlt, timestamp)){
                                isCIAdded = true;
                            }
                        }
                    }
                }
            }
        }
        // decide if the ci need to be pushed
        long ciTimestamp = Long.parseLong(currentEntity.getAttributeMap().get(SimpleTopology.CLASSMODEL_ATTRNAME_LASTACCESSTIME));
        if(!isCIAdded && ciTimestamp < timestamp){
            rlt.getBatchStatistics().incSkippedCICount();
            return isCIAdded;
        }
        isCIAdded = true;
        // add ci
        rlt.addCIEntity(currentEntity);
        if(parentId != null && parentRelationName != null){
            CIRelation relation = new CIRelation(parentRelationName, parentId, currentEntity.getGlobalId());
            rlt.addRelation(relation);
        }
        return isCIAdded;
    }

    private static CIEntity buildCIFromJsonNode(JsonNode jsonNode, SimpleTopology simpleTopology) {
        CIEntity rlt = null;
        String globalid;
        if(jsonNode.get(PAYLOAD_FIELD_GLOBAL_ID) != null || jsonNode.get(PAYLOAD_FIELD_CMDB_ID) != null){
            if(jsonNode.get(PAYLOAD_FIELD_GLOBAL_ID) != null){
                globalid = jsonNode.get(PAYLOAD_FIELD_GLOBAL_ID).asText();
            } else{
                globalid = jsonNode.get(PAYLOAD_FIELD_CMDB_ID).asText();
            }

            rlt = new CIEntity(globalid, simpleTopology.getClassname());
            for(AttributeMeta attrMeta : pushRepository.getClassTypeMetaMap().get(simpleTopology.getClassname()).getAttributeMetaList()){
                if(jsonNode.get(attrMeta.getAttrName()) != null){
                    rlt.getAttributeMap().put(attrMeta.getAttrName(), RESPONSE_FIELD_NULL.equals(jsonNode.get(attrMeta.getAttrName()).asText())?null:jsonNode.get(attrMeta.getAttrName()).asText());
                }
            }
        } else{
            log.warn("[UCMDB]retrieved ci without globalid: " + jsonNode);
        }
        return rlt;
    }
}
