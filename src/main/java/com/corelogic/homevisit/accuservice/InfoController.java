package com.corelogic.homevisit.accuservice;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class InfoController {

    @RequestMapping("/")
    public String info(HttpServletRequest req) {

        return "<!DOCTYPE html><html><head>\n" +
                "    <meta http-equiv=\"Refresh\" content=\"0; url=/actuator\" />\n" +
                "</head><body>\n" +
                "    <p>Please follow <a href=\"/actuator\">this link</a>.</p>\n" +
                "</body></html>";

    }

}
