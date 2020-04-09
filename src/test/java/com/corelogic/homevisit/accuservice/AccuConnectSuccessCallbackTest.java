package com.corelogic.homevisit.accuservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.sql.DataSource;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = "localtest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class AccuConnectSuccessCallbackTest {

    private static final String REQ = "{" +
            "\"batchDate\": \"date\"," +
            "\"batchNumber\": 123," +
            "\"batchTemplateId\": \"string\"," +
            "\"mailClass\": \"string\"," +
            "\"listFileName\": \"string\"," +
            "\"printFileName\": \"string\"," +
            "\"batchInfo\": [{" +
            "\"itemId\": 123," +
            "\"requestId\": \"15\"," +
            "\"orderDate\": \"date\"," +
            "\"productCode\": \"string\"," +
            "\"quantity\": 123" +
            "}]" +
            "}";

    private static final String REQ16 = "{" +
            "\"batchDate\": \"date\"," +
            "\"batchNumber\": 123," +
            "\"batchTemplateId\": \"string\"," +
            "\"mailClass\": \"string\"," +
            "\"listFileName\": \"string\"," +
            "\"printFileName\": \"string\"," +
            "\"batchInfo\": [{" +
            "\"itemId\": 123," +
            "\"requestId\": \"16\"," +
            "\"orderDate\": \"date\"," +
            "\"productCode\": \"string\"," +
            "\"quantity\": 123" +
            "}]" +
            "}";

    @Autowired
    private DataSource dataSource;

    @Value("${corelogic.accuservice.callback.apikey}")
    private String callbackApiKey;

    @Value("${corelogic.accuservice.callback.path}")
    private String callbackApiPath;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testInboundGateway() throws Exception {

        mockMvc.perform(post(callbackApiPath)
                .param("apikey", callbackApiKey)
                .content(REQ))
                .andExpect(status().isOk());

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 15");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("PROCESSED", map.get("ACC_STATUS"));
    }

    @Test
    public void testInboundGatewayHttps() throws Exception {

        disableCertificateVerification();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<String>(REQ16, headers);

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("https://localhost:18443" + callbackApiPath + "?apikey=" + callbackApiKey,
                request,  String.class);

        JdbcPollingChannelAdapter jpca = new JdbcPollingChannelAdapter(dataSource,
                "select * from printRequest where id = 16");
        Message checkRez = jpca.receive();
        List lst = (List) checkRez.getPayload();
        Map map = (Map) lst.get(0);
        assertEquals("PROCESSED", map.get("ACC_STATUS"));
    }

    private void disableCertificateVerification() throws Exception {
        final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }
        } };

        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname, session) -> hostname.equals("localhost"));

    }

}
