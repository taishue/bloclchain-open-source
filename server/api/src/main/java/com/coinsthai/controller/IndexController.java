package com.coinsthai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String indexPage(ModelMap map) {
        return "index";
    }

}
