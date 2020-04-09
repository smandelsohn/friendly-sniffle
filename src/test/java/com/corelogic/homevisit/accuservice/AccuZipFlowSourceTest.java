package com.corelogic.homevisit.accuservice;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.core.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT )
@ActiveProfiles(profiles = "sourcetest")
@DirtiesContext
public class AccuZipFlowSourceTest {

    @Autowired
    @Qualifier("printRequestVerificationMessageSource")
    private MessageSource<Object> printRequestVerificationMessageSource;

    @Autowired
    @Qualifier("toPickupAddressesChannel")
    PublishSubscribeChannel toPickupAddressesChannel;

    /**
     * Test that we abe to pick up print requests for validation in status NEW only.
     */
    @Test
    public void pickupPrintRequestStatusinNewStatus() {

        final QueueChannel toCsvMessageChannelTrap = new QueueChannel();
        toPickupAddressesChannel.addInterceptor(new WireTap(toCsvMessageChannelTrap));

        toCsvMessageChannelTrap.receive(5000);

        List lst = ((List)printRequestVerificationMessageSource.receive().getPayload());
        lst.forEach(
                r -> {
                    Map m = (Map)r;
                    String statuss = (String) m.get("ACC_STATUS");
                    assertTrue("NEW".equals(statuss));
                }
        );
        assertTrue(!lst.isEmpty());
    }

}
