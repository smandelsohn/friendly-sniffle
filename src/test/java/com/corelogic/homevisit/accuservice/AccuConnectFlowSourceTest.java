package com.corelogic.homevisit.accuservice;

import com.corelogic.homevisit.accuservice.service.impl.AccuTestController;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@ActiveProfiles(profiles = "sourcetest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT )
@DirtiesContext
public class AccuConnectFlowSourceTest {

    @Autowired
    public PublishSubscribeChannel toUpdateSecurityToken;

    @Autowired
    public IntegrationFlow updateSecurityTokenFlow;

    @Autowired
    @Qualifier("readyForConnectMessageSource")
    private MessageSource<Object> readyForConnectMessageSource;

    private static class Expectations {
        Long addCount;
        String forntVar;
        String backVar;
        String placement;

        public Expectations(Long addCount, String forntVar, String backVar, String placement) {
            this.addCount = addCount;
            this.forntVar = forntVar;
            this.backVar = backVar;
            this.placement = placement;
        }
    }


    @Test
    public void testJobReadyMessageSource() {
        int checkCnt = 0;
        List lst = (List) readyForConnectMessageSource.receive().getPayload();

        assertEquals(4, lst.size());

        Map<Integer, Expectations> exp = new HashMap<Integer, Expectations>() {{
           put(500, new Expectations(1L, null, "285587_24833-23509_2.pdf", null));
           put(200, new Expectations(3L, "285567_24822-23498_1.pdf", "285567_24822-23498_2.pdf", null));
           put(300, new Expectations(1L, "285568_24823-23499_1.pdf", "285568_24823-23499_2.pdf", "HP"));
           put(400, new Expectations(0L, "285573_24826-23502_1.pdf", "285573_24826-23502_2.pdf", null));
        }};
        for (int i = 0; i < lst.size(); i++) {
            Map<String, Object> record = (Map<String, Object>) lst.get(i);
            Expectations rexp = exp.get(NumberUtils.toInt(record.get("CARTID_ID").toString()));
            assertNotNull(rexp);
            assertEquals(rexp.addCount, record.get("ADD_COUNT"));
            assertEquals(rexp.forntVar, record.get("FRONTVAR"));
            assertEquals(rexp.backVar, record.get("BACKVAR"));
            assertEquals(" Record is " + record, rexp.placement, record.get("POSTAGEPLACEMENT"));
            checkCnt++;
        }
        assertEquals(4, checkCnt);

    }

    @Test
    public void tokenExpiredMessageSource() throws InterruptedException {


        final QueueChannel channelTrap = new QueueChannel();
        toUpdateSecurityToken.addInterceptor(new WireTap(channelTrap));

        updateSecurityTokenFlow.getInputChannel().send(new GenericMessage<>(Boolean.TRUE));

        Message<Object> obj = (Message<Object>) channelTrap.receive(1000);

        assertNotNull(obj);

        assertNotNull(obj.getPayload());

        String responseWithToken = obj.getPayload().toString();

        assertTrue( responseWithToken.contains("U29Zb3VEZWNpZGVUb0xvb2tJbnRvUmFiYml0SG9sZTopTmljZQ==") );

        updateSecurityTokenFlow.getInputChannel().send(new GenericMessage<>(Boolean.TRUE));

        obj = (Message<Object>) channelTrap.receive(1000);

        assertNotEquals(responseWithToken, obj.getPayload().toString());

        responseWithToken = obj.getPayload().toString();

        assertTrue( responseWithToken.contains("U29Zb3VEZWNpZGVUb0xvb2tJbnRvUmFiYml0SG9sZTopTmljZQ==") );

        assertTrue(AccuTestController.tokenReqBody.contains("DouglasAdams"));
        assertTrue(AccuTestController.tokenReqBody.contains("42"));

    }



}
