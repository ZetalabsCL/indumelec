package com.zetalabs.indumelec.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
    @RequestMapping(value={"/admin/dashboard"}, method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/dashboard");

        return modelAndView;
    }

    @RequestMapping(value={"/admin/review"}, method = RequestMethod.POST)
    public ModelAndView reviewQuote(@RequestParam("quoteReviewId") Long quoteReviewId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/review");
        modelAndView.addObject("quoteId", quoteReviewId);

        return modelAndView;
    }
}