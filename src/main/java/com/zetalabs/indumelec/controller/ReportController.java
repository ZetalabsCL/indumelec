package com.zetalabs.indumelec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportController {
    @RequestMapping(value={"/report/dashboard"}, method = RequestMethod.GET)
    public ModelAndView dashboard(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("report/dashboard");

        return modelAndView;
    }
}
