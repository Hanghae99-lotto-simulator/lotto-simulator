package com.lotto.lotto_simulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/swagger")
class SwaggerRedirectController {
    @GetMapping
    public String api() { return "redirect:/swagger-ui/index.html"; }
}