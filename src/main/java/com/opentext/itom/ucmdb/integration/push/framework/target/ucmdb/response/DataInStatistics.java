package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Statistics returned from dataIn operations
 */
public class DataInStatistics {

    @JsonProperty("addedCis")
    public List<String> added;

    @JsonProperty("removedCis")
    public List<String> removed;

    @JsonProperty("updatedCis")
    public List<String> updated;

    @JsonProperty("ignoredCis")
    public List<String> ignored;

    @JsonProperty("multipleMatchIgnored")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean multipleMatchIgnored;

    @JsonProperty("idsMap")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Map<String, String> idsMap;

    @JsonProperty("ignoredMap")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String,String> ignoredMap;

    @JsonProperty("failureMap")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Map<String, DataInFailure> failureMap;

    public Map<String, String> getIdsMap() {
        return idsMap;
    }

    public List<String> getIgnored() {
        return ignored;
    }

    public Map<String, DataInFailure> getFailureMap() {
        return failureMap;
    }
}
