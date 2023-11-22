package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.payload;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A topology data element
 */
public class DataInElement {

    @JsonProperty("ucmdbId")
    public String ucmdbId;
    @JsonProperty("type")
    public String type;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("properties")
    public Map<String, String> properties;

    public Map<String, String> getProperties() {
        if(properties == null){
            properties = new HashMap<String, String>();
        }
        return properties;
    }

    public String getType() {
        return type;
    }

    public DataInElement(String ucmdbId, String type) {
        this.ucmdbId = ucmdbId;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataInElement)) return false;
        DataInElement that = (DataInElement) o;
        return Objects.equals(ucmdbId, that.ucmdbId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucmdbId);
    }
}
