package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.itom.ucmdb.client.UCMDBConfiguration;
import com.opentext.itom.ucmdb.client.rest.wrapper.ClassModelClassWrapper;
import com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.payload.CIBatchPayload;
import com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.payload.UCMDBRestPayloadConverter;
import com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.response.DataInStatistics;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component
public class UCMDBRestClient {
    private static final Logger log = LoggerFactory.getLogger(UCMDBRestClient.class);
    @Autowired
    TargetUCMDBConfiguration targetconf;
    private String token;
    private String baseURL;

    public String getToken() {
        return token;
    }

    public void authenticate() throws Exception {
        String hostname = targetconf.getTargetHostname();
        String port = targetconf.getTargetPort();
        String username = targetconf.getTargetUsername();
        String password = targetconf.getTargetPassword();
        baseURL = "https://" + hostname +":" + port + "/rest-api/";

        String body = "{\n" +
                "\t\"username\":\"" + username + "\",\n" +
                "\t\"password\":\"" + password + "\"\n" +
                "\t\"clientContext\": " + targetconf.getCustomerid() +
                "}";

        String result = doPost(baseURL + "authenticate", body);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(result);
        token = jsonNode.get("token").textValue();
    }

    protected String doGet(String url) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        setHeaders(httpGet);

        CloseableHttpResponse httpResponse = sendRequest(httpGet);

        result = EntityUtils.toString(httpResponse.getEntity());
        return result;
    }


    protected String doPost(String url, String body) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        setHeaders(httpPost);
        StringEntity entity = new StringEntity(body);
        httpPost.setEntity(entity);

        CloseableHttpResponse httpResponse = sendRequest(httpPost);

        result = EntityUtils.toString(httpResponse.getEntity());

        return result;
    }

    private CloseableHttpResponse sendRequest(HttpRequestBase request) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        CloseableHttpResponse httpResponse = null;
        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustAllStrategy()).build();;
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, (s, sslSession) -> true);
        CloseableHttpClient c = HttpClients
                .custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setSSLHostnameVerifier(new TrustAnyHostnameVerifier())
                .build();

        httpResponse = c.execute(request);
        return httpResponse;
    }

    private void setHeaders(HttpRequestBase request) {
        request.setHeader("Authorization", "Bearer " + token);
        request.setHeader("Content-Type", "application/json;charset=utf-8");
        request.setHeader("Accept", "application/json");
    }


    public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public DataInStatistics batchDatain(CIBatchPayload ciBatchPayload) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.debug("[Push2UCMDB]Body:" + objectMapper.writeValueAsString(ciBatchPayload));
        String result = doPost(baseURL + "dataModel/?forceTemporaryId=true&returnIdsMap=true", objectMapper.writeValueAsString(ciBatchPayload));
        log.debug("[Push2UCMDB]Response:" + result);
        DataInStatistics rlt = objectMapper.readValue(result, DataInStatistics.class);
        return rlt;
    }

}
