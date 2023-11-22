package com.opentext.itom.ucmdb.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UCMDBConfiguration {
    @Value("${ucmdb.customerid}")
    private String customerId;

    @Value("${ucmdb.hostname}")
    private String hostname;

    @Value("${ucmdb.port}")
    private String ucmdbPort;

    @Value("${ucmdb.gateway.hostname}")
    private String gatewayHostname;

    @Value("${ucmdb.gateway.port}")
    private String gatewayPort;

    @Value("${ucmdb.username}")
    private String ucmdbUsername;

    @Value("${ucmdb.password}")
    private String ucmdbPassword;

    public String getHostname() {
        return hostname;
    }

    public String getUcmdbPort() {
        return ucmdbPort;
    }

    public String getUcmdbUsername() {
        return ucmdbUsername;
    }

    public String getUcmdbPassword() {
        return ucmdbPassword;
    }

    public String getGatewayHostname() {
        return gatewayHostname;
    }

    public String getGatewayPort() {
        return gatewayPort;
    }

    public String getCustomerId() {
        return customerId;
    }
}
