package com.opentext.itom.ucmdb.client.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import com.opentext.itom.ucmdb.client.UCMDBClient;
import com.opentext.itom.ucmdb.client.graphql.resultwrapper.ResponseWrapper;
import com.opentext.itom.ucmdb.client.rest.wrapper.ClassModelClassWrapper;
import com.opentext.itom.ucmdb.integration.push.framework.PushRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("graphql")
public class UCMDBGraphQLClient implements UCMDBClient {
    private static final Logger log = LoggerFactory.getLogger(UCMDBGraphQLClient.class);
    public static final String RESPONSE_COUNT = "COUNT";
    @Autowired
    GraphQLClient graphQLClient;

    @Override
    public boolean testConnection() {
        boolean rlt = false;
        try {
            graphQLClient.authenticate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(graphQLClient.getToken() != null){
            rlt = true;
        }
        return rlt;
    }

    @Override
    public ClassModelClassWrapper getClassDefByName(String classname) {
        return null;
    }

    public int countElement(SingleTopoQuery query) {
        int rlt = 0;
        try {
            graphQLClient.authenticate();
            ResponseWrapper responseWrapper = graphQLClient.executeGraphQLQuery(query.generateCountPayload());
            JsonNode jsonNode = responseWrapper.getData();
            rlt = jsonNode.get(query.getElement().getElementName()).get(0).get(RESPONSE_COUNT).asInt();
        } catch (Exception e) {
            log.error("[GraphQL]Failed to run Graphql.");
            e.printStackTrace();
        }
        return rlt;
    }

    public ResponseWrapper batchQuery(SingleTopoQuery query){
        ResponseWrapper rlt = null;
        try {
            rlt = graphQLClient.executeGraphQLQuery(query.generateBatchQueryPayload());
        } catch (Exception e) {
            log.error("[GraphQL]Failed to get CI via Graphql.");
            e.printStackTrace();
        }
        return rlt;
    }
}
