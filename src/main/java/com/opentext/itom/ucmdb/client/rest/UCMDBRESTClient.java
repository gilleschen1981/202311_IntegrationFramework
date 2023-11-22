package com.opentext.itom.ucmdb.client.rest;

import com.opentext.itom.ucmdb.client.UCMDBClient;
import com.opentext.itom.ucmdb.client.rest.wrapper.ClassModelClassWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("rest")
public class UCMDBRESTClient implements UCMDBClient {
    @Autowired
    RestClient restClient;
    @Override
    public boolean testConnection() {
        boolean rlt = false;
        try {
            restClient.authenticate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(restClient.getToken() != null){
            rlt = true;
        }
        return rlt;
    }

    @Override
    public ClassModelClassWrapper getClassDefByName(String classname) {
        ClassModelClassWrapper rlt = null;
        try {
            restClient.authenticate();
            rlt = restClient.getClassDefinitionByName(classname);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rlt;
    }
}
