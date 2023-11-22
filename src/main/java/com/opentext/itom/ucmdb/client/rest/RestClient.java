package com.opentext.itom.ucmdb.client.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.itom.ucmdb.client.UCMDBConfiguration;
import com.opentext.itom.ucmdb.client.UCMDBHttpClient;
import com.opentext.itom.ucmdb.client.rest.wrapper.ClassModelClassWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component
public class RestClient extends UCMDBHttpClient {
    private String baseURL;
    @Autowired
    UCMDBConfiguration ucmdbConf;

    public void authenticate() throws Exception {
        String hostname = ucmdbConf.getHostname();
        String port = ucmdbConf.getUcmdbPort();
        String username = ucmdbConf.getUcmdbUsername();
        String password = ucmdbConf.getUcmdbPassword();
        baseURL = "https://" + hostname +":" + port + "/rest-api/";

        String body = "{\n" +
                "\t\"username\":\"" + username + "\",\n" +
                "\t\"password\":\"" + password + "\"\n" +
                "\t\"clientContext\":\"" + ucmdbConf.getCustomerId() + "\"\n" +
                "}";

        String result = doPost(baseURL + "authenticate", body);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);
        token = jsonNode.get("token").textValue();
    }

    public ClassModelClassWrapper getClassDefinitionByName(String classname) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String result = doGet(baseURL + "classModel/citypes/" + classname);
        ObjectMapper objectMapper = new ObjectMapper();
        ClassModelClassWrapper wrapper = objectMapper.readValue(result, ClassModelClassWrapper.class);
        return wrapper;
    }


}
