package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.payload;

import com.opentext.itom.ucmdb.integration.push.repo.PushRepository;
import com.opentext.itom.ucmdb.integration.push.repo.model.AttributeTypeEnum;
import com.opentext.itom.ucmdb.integration.push.repo.model.ClassTypeMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIEntity;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIRelation;
import jakarta.annotation.PostConstruct;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class UCMDBRestPayloadConverter {
    private static final Logger log = LoggerFactory.getLogger(UCMDBRestPayloadConverter.class);
    public static final String REST_PAYLOAD_ATTRIBUTE_GLOBAL_ID = "global_id";
    public static final String REST_PAYLOAD_ATTRIBUTE_ROOT_CLASS = "root_class";

    static PushRepository pushRepository;
    @Autowired
    PushRepository autopushRepository;

    @PostConstruct
    public void init(){
        pushRepository = this.autopushRepository;
    }


    public static CIBatchPayload convertCIBatch2Payload(CIBatch ciBatch) {
        CIBatchPayload rlt = new CIBatchPayload();
        for(String ciid : ciBatch.getCiEntityMap().keySet()){
            CIEntity ciEntity = ciBatch.getCiEntityMap().get(ciid);
            DataInConfigurationItem ci = new DataInConfigurationItem(ciEntity.getGlobalId(), ciEntity.getAccurateType());
            for(String attrName : ciEntity.getAttributeMap().keySet()){
                String attrValue = ciEntity.getAttributeMap().get(attrName);
                ClassTypeMeta classTypeMeta = pushRepository.getClassTypeMetaMap().get(ciEntity.getCiType());
                if(classTypeMeta == null){
                    log.debug("[Push]ClassTypeMeta missing: " + ciEntity.getCiType());
                    continue;
                } else{
                    AttributeTypeEnum attrType = classTypeMeta.getAttrTypeFromName(attrName);
                    if(attrType == null){
                        log.debug("[Push]AttrMeta error, citype = : " + ciEntity.getCiType() + ". Attribute " + attrName + " has null type.");
                        continue;
                    }else {
                        attrValue = convertAttrValue(attrType, attrValue);
                    }
                }
                ci.getProperties().put(attrName, attrValue);
            }
            // add globalid
            ci.getProperties().put(REST_PAYLOAD_ATTRIBUTE_GLOBAL_ID, ciEntity.getGlobalId());
            ci.getProperties().put(REST_PAYLOAD_ATTRIBUTE_ROOT_CLASS, ciEntity.getAccurateType());
            rlt.getCis().add(ci);
        }
        for(Set<CIRelation> relationSet : ciBatch.getParentMap().values()){
            for(CIRelation relation : relationSet){
                DataInRelation dataInRelation = new DataInRelation(relation.getGlobalId(), relation.getCiType(), relation.getEnd1Id(), relation.getEnd2Id());
                for(String attr : relation.getAttributeMap().keySet()){
                    dataInRelation.getProperties().put(attr, relation.getAttributeMap().get(attr));
                }
                dataInRelation.getProperties().put(REST_PAYLOAD_ATTRIBUTE_ROOT_CLASS, relation.getCiType());
                rlt.getRelations().add(dataInRelation);
            }
        }
        return rlt;
    }

    private static String convertAttrValue(AttributeTypeEnum attrType, String attrValue) {
        if(attrValue == null){
            return  null;
        }
        String rlt = attrValue;
        switch (attrType){
            case ATTR_TYPE_DATE -> {
                DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime().withZoneUTC();
                try{
                    rlt = new DateTime(new Date(Long.parseLong(attrValue))).toString(dateTimeFormatter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            case ATTR_TYPE_STRING,ATTR_TYPE_INTEGER -> {}
            default -> {}
        }
        return rlt;
    }
}
