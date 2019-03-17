package com.zetalabs.indumelec.controller.admin;

import com.zetalabs.indumelec.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value={"/admin/dashboard"}, method = RequestMethod.GET)
    public ModelAndView dashboard(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/dashboard");

        model.addAttribute("quoteList", quoteService.getQuoteList());
        model.addAttribute("statistic", quoteService.getStatistic());
        model.addAttribute("salesProjection", quoteService.getSalesProjection());

        return modelAndView;
    }
}
