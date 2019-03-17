package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value={"/user/dashboard"}, method = RequestMethod.GET)
    public ModelAndView dashboard(HttpSession session, Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/dashboard");

        User loggedUser = (User) session.getAttribute("user");

        model.addAttribute("quoteList", quoteService.getQuoteList());
        model.addAttribute("quotesInformation", quoteService.getQuotesInformation());

        return modelAndView;
    }
}
