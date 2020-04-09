package com.corelogic.homevisit.accuservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = "localtest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT )
@DirtiesContext
class AccuConnectConfigurationTest {

    @Autowired
    AccuConnectConfiguration accuConnectConfiguration;

    @Test
    void composeAwsUrl() throws MalformedURLException {
        assertEquals("https://homevisit-dev.s3.amazonaws.com/img/285573/pdf/abc.pdf",
                accuConnectConfiguration.composeAwsUrl("abc.pdf",285573));
        assertEquals("https://homevisit-dev.s3.amazonaws.com/img/285573/pdf//abc.pdf",
                accuConnectConfiguration.composeAwsUrl("/abc.pdf",285573));
        assertEquals(null, accuConnectConfiguration.composeAwsUrl(null,285573));
        assertEquals(null, accuConnectConfiguration.composeAwsUrl(" ",285573), "Nothing to print");
    }
}