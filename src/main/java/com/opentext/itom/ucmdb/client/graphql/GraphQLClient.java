package com.opentext.itom.ucmdb.client.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.itom.ucmdb.client.UCMDBConfiguration;
import com.opentext.itom.ucmdb.client.UCMDBHttpClient;
import com.opentext.itom.ucmdb.client.graphql.resultwrapper.ResponseWrapper;
import com.opentext.itom.ucmdb.integration.push.framework.PushRunner;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component()
public class GraphQLClient extends UCMDBHttpClient {
    private static final Logger log = LoggerFactory.getLogger(GraphQLClient.class);
    private String baseURL;
    @Autowired
    UCMDBConfiguration ucmdbConf;

    public void authenticate() throws Exception {
        String hostname = ucmdbConf.getGatewayHostname();
        String port = ucmdbConf.getGatewayPort();
        String username = ucmdbConf.getUcmdbUsername();
        String password = ucmdbConf.getUcmdbPassword();
        baseURL = "https://" + hostname +":" + port + "/cms-gateway/";

        String body = "{\n" +
                "\t\"username\":\"" + username + "\",\n" +
                "\t\"password\":\"" + password + "\"\n" +
                "}";

        String result = doPost(baseURL + "urest/v1/" + ucmdbConf.getCustomerId() +"/authentication?target=cms", body);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);
        token = jsonNode.get("token").textValue();
    }

    public ResponseWrapper executeGraphQLQuery(String query) throws Exception {
        JSONObject queryJson = new JSONObject();
        queryJson.put("query",query);
        JSONObject variables = new JSONObject();
        JSONObject requestBody = new JSONObject();
        requestBody.put("query", queryJson.getString("query"));
        requestBody.put("variables", variables);

        log.debug("[GraqhQLCall]Body:" + requestBody.toString());
        String result = doGraphQLPost(baseURL + ucmdbConf.getCustomerId() + "/graphql", requestBody.toString());
        log.debug("[GraqhQLCall]Response:" + result);
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseWrapper responseWrapper = objectMapper.readValue(result, ResponseWrapper.class);
        return responseWrapper;
    }

}
