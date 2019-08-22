package com.marcus.startup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Charles
 * @since 2019-07-08
 */
@Controller
public class HomeController {



    @RequestMapping("main")
    public String main(){

        return "main/index";
    }


    @RequestMapping("blank")
    public String blank(){

        return "blank/blank";
    }
}
