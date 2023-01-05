package hust.iot.kien.restapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
    
    @GetMapping(value = "/")
    public String index() {
        return "index";
    }
}
