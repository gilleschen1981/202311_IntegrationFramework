package com.opentext.itom.ucmdb.client.graphql.resultwrapper;

import com.fasterxml.jackson.databind.JsonNode;

public class ResponseWrapper {
    private ExtensionsWrapper extensions;
    private JsonNode data;

    public ExtensionsWrapper getExtensions(){
        return extensions;
    }

    public JsonNode getData(){
        return data;
    }
}
