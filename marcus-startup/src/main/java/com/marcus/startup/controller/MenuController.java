package com.marcus.startup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Charles
 * @since 2019-07-10
 */
@Controller
public class MenuController {



    @RequestMapping("menu")
    public String menu(){

        return "menu/menu";
    }
}
