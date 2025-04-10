package com.seplag.apiseplag.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SwaggerRedirectController {

    @GetMapping("/")
    public RedirectView redirectToSwaggerUi() {
        return new RedirectView("/swagger-ui/index.html");
    }
    
    @GetMapping("/swagger-ui")
    public RedirectView redirectToSwaggerUiAlternate() {
        return new RedirectView("/swagger-ui/index.html");
    }
} 