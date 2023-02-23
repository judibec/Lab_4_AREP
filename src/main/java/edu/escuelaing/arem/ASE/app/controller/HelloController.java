package edu.escuelaing.arem.ASE.app.controller;

import edu.escuelaing.arem.ASE.app.controller.annotations.Component;
import edu.escuelaing.arem.ASE.app.controller.annotations.RequestMapping;

@Component
public class HelloController {
    @RequestMapping(value = "/hello")
    public static String index() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-type: text/html\r\n" +
                "\r\n" + "Greetings from Spring Boot!";
    }
}
