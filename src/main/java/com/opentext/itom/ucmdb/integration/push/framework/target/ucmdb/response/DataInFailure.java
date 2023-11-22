package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataInFailure {
    @JsonProperty
    public int errorCode;

    @JsonProperty
    public String message;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<String> parameterValues;
}
