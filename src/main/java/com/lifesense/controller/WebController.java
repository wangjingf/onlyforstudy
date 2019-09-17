package com.lifesense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web")
public class WebController {

    @RequestMapping(value = "/json", method = { RequestMethod.GET })
    public String welCome() {

        return "redirect:/json.html";
    }
}
