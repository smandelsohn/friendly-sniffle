package com.corelogic.homevisit.accuservice.component;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class SecurityTokenHolderTest {

    @Test
    public void isExpired() throws InterruptedException {

        SecurityTokenHolder sth = new SecurityTokenHolder();

        assertTrue(sth.isExpired());

        sth.updateToken("ZaphordBeebblebrox", 1);

        assertFalse(sth.isExpired());
        assertNotNull(sth.getToken());
        Thread.sleep(1001);
        assertTrue(sth.isExpired());
        assertNull(sth.getToken());

    }

}