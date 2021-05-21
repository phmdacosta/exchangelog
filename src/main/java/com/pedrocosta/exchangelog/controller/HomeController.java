package com.pedrocosta.exchangelog.controller;

import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.services.WebService;
import com.pedrocosta.exchangelog.utils.DateUtils;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    Logger logger = LogManager.getLogger(HomeController.class);

    private WebService service;
    private GsonUtils gsonUtils;

    public HomeController(WebService service, GsonUtils gsonUtils) {
        this.service = service;
        this.gsonUtils = gsonUtils;
    }

    @RequestMapping("/")
    public String index() {

        return "index";
    }

    @RequestMapping(value = "/now", produces = MediaType.APPLICATION_JSON_VALUE)
    public String now(@RequestParam String from, @RequestParam(required = false) String[] to,
                      @RequestParam(required = false) double amount) {
        ServiceResponse<List<Exchange>> result = service.getQuoteRate(
                from, to, amount);
        String json = gsonUtils.toJson(result);
        System.out.println(json);
        return json;
    }

    @RequestMapping(value = "/backdated", produces = MediaType.APPLICATION_JSON_VALUE)
    public String backdated(@RequestParam String from, @RequestParam(required = false) String[] to,
                            @RequestParam String date, @RequestParam(required = false) double amount) {
        ServiceResponse<List<Exchange>> result = service.getQuoteRate(
                from, to, amount, DateUtils.stringToDate(date));
        String json = gsonUtils.toJson(result);
        System.out.println(json);
        return json;
    }

}
