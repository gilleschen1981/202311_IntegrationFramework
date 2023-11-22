package com.opentext.itom.ucmdb.client;

import com.opentext.itom.ucmdb.client.rest.wrapper.ClassModelClassWrapper;

public interface UCMDBClient {
    boolean testConnection();

    ClassModelClassWrapper getClassDefByName(String classname);
}
