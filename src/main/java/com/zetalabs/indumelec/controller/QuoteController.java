package com.zetalabs.indumelec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class QuoteController {
    @RequestMapping(value={"/shared/newquote"}, method = RequestMethod.GET)
    public ModelAndView dashboard(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shared/newquote");

        return modelAndView;
    }
}
