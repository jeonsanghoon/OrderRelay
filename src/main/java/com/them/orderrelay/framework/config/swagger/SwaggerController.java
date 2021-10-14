package com.them.orderrelay.framework.config.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class SwaggerController {
    @GetMapping(value = "/")
    public String index()
    {
        return "redirect:swagger-ui.html";
    }
}
