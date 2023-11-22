package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TargetUCMDBConfiguration {
    @Value("${push.target.hostname}")
    private String targetHostname;

    @Value("${push.target.port}")
    private String targetPort;

    @Value("${push.target.username}")
    private String targetUsername;

    @Value("${push.target.password}")
    private String targetPassword;

    @Value("${push.target.customerid}")
    private String customerid;

    public String getCustomerid() {
        return customerid;
    }

    public String getTargetHostname() {
        return targetHostname;
    }

    public String getTargetPort() {
        return targetPort;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public String getTargetPassword() {
        return targetPassword;
    }
}
