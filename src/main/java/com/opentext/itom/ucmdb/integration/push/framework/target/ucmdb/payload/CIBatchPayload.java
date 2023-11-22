package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.management.relation.Relation;
import java.util.ArrayList;
import java.util.List;

public class CIBatchPayload {
    @JsonProperty("cis")
    public List<DataInConfigurationItem> cis;

    @JsonProperty("relations")
    public List<DataInRelation> relations;

    public List<DataInConfigurationItem> getCis() {
        if(cis == null){
            cis = new ArrayList<DataInConfigurationItem>();
        }
        return cis;
    }

    public List<DataInRelation> getRelations() {
        if(relations == null){
            relations = new ArrayList<DataInRelation>();
        }
        return relations;
    }
}
