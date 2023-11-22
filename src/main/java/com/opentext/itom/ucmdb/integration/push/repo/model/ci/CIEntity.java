package com.opentext.itom.ucmdb.integration.push.repo.model.ci;

import com.opentext.itom.ucmdb.integration.push.configuration.SimpleTopology;
import com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.payload.UCMDBRestPayloadConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CIEntity {
    private static final Logger log = LoggerFactory.getLogger(CIEntity.class);
    private String globalId;

    private String ciType;

    private Map<String, String> attributeMap;

    public Map<String, String> getAttributeMap() {
        if(attributeMap == null){
            attributeMap = new HashMap<String, String>();
        }
        return attributeMap;
    }

    public CIEntity(String globalId, String ciType) {
        this.globalId = globalId;
        this.ciType = ciType;
    }

    public String getGlobalId() {
        return globalId;
    }

    public String getCiType() {
        return ciType;
    }

    public String getAccurateType(){
        if(getAttributeMap().get(SimpleTopology.CLASSMODEL_ATTRNAME_CLASSTYPE) != null){
            return getAttributeMap().get(SimpleTopology.CLASSMODEL_ATTRNAME_CLASSTYPE);
        }
        // missing root_class
        log.error("[Push]root_class missing, ciid = " + getGlobalId() + ". CI Type = " + getCiType());
        return ciType;
    }
}
