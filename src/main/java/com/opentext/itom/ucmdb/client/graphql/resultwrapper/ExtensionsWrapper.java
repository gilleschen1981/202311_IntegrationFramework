package com.opentext.itom.ucmdb.client.graphql.resultwrapper;

import com.fasterxml.jackson.databind.JsonNode;

public class ExtensionsWrapper {
    private JsonNode bulkOperationErrors;

    public JsonNode getBulkOperationErrors() {
        return bulkOperationErrors;
    }
}
