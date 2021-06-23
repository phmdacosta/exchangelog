package com.pedrocosta.exchangelog.controller;

import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.services.ExchangeService;
import com.pedrocosta.exchangelog.services.ServiceFactory;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.utils.DateUtils;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller to handle exchange requests.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
@Controller
public class ExchangeController {

    private ExchangeService exchangeService;
    private GsonUtils gsonUtils;

    public ExchangeController(ServiceFactory serviceFactory, GsonUtils gsonUtils) {
        this.exchangeService = (ExchangeService) serviceFactory.create(Exchange.class);
        this.gsonUtils = gsonUtils;
    }

    @GetMapping(value = "/now", produces = MediaType.APPLICATION_JSON_VALUE)
    public String now(@RequestParam String from,
                      @RequestParam(required = false) String[] to,
                      @RequestParam(required = false) double amount) {
        ServiceResponse<List<Exchange>> result = ServiceResponse.<List<Exchange>>createSuccess()
                .setObject(new ArrayList<>());

        if (isValidParameters(from)) {
            return toJson(ServiceResponse.createError(HttpStatus.BAD_REQUEST,
                    Messages.get("error.request.param")));
        }

        if (to == null || to.length == 0) {
            result.setObject(exchangeService.findAll(new Currency(from), amount));
        } else {
            for (String toCcy : to) {
                Exchange exchange = exchangeService.findLast(
                        new Currency(from), new Currency(toCcy), amount);
                if (exchange != null) {
                    result.getObject().add(exchange);
                }
            }
        }

        if (result.getObject().isEmpty()) {
            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                    Messages.get("error.no.exch.found"));
        }
        else if (result.getObject().size() == 1) {
            // If list has just one data, return a json with single info
            return toJson(ServiceResponse.<Exchange>createSuccess()
                    .setObject(result.getObject().get(0)));
        }

        return toJson(result);
    }

    @GetMapping(value = "/backdated", produces = MediaType.APPLICATION_JSON_VALUE)
    public String backdated(@RequestParam String from, @RequestParam(required = false) String[] to,
                            @RequestParam(required = false) double amount, @RequestParam String date) {
        ServiceResponse<List<Exchange>> result = ServiceResponse.<List<Exchange>>createSuccess()
                .setObject(new ArrayList<>());

        Date valDate = DateUtils.stringToDate(date);
        if (isValidParameters(from) && valDate != null) {
            return toJson(ServiceResponse.createError(HttpStatus.BAD_REQUEST,
                    Messages.get("error.request.param")));
        }

        if (to == null || to.length == 0) {
            result.setObject(exchangeService.findAll(new Currency(from),
                    valDate, amount));
        } else {
            for (String toCcy : to) {
                Exchange exchange = exchangeService.find(new Currency(from),
                        new Currency(toCcy), valDate, amount);
                if (exchange != null) {
                    result.getObject().add(exchange);
                }
            }
        }

        if (result.getObject().isEmpty()) {
            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                    Messages.get("error.no.exch.found"));
        }
        else if (result.getObject().size() == 1) {
            // If list has just one data, return a json with single info
            return toJson(ServiceResponse.<Exchange>createSuccess()
                    .setObject(result.getObject().get(0)));
        }

        return toJson(result);
    }

    private String toJson(ServiceResponse<?> response) {
        String json = gsonUtils.toJson(response);
        Log.info(this, json);
        return json;
    }

    private boolean isValidParameters(Object ... params) {
        for (Object p : params) {
            if (p == null) {
                return false;
            }
        }
        return true;
    }
}
