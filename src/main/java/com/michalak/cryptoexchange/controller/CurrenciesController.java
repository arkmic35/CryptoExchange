package com.michalak.cryptoexchange.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/currencies/")
@Log4j2
public class CurrenciesController {

    @GetMapping("{currency}")
    public void getCurrency(@PathVariable("currency") String currency,
                            @RequestParam(value = "filter[]", required = false) String[] rateFilters) {

        log.info("GET /currencies/{} Filter: {}", currency, rateFilters);
    }
}
