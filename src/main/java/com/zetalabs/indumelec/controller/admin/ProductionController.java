package com.zetalabs.indumelec.controller.admin;

import com.zetalabs.indumelec.model.types.Status;
import com.zetalabs.indumelec.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductionController {
    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value={"/admin/production"}, method = RequestMethod.GET)
    public ModelAndView dashboard(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/production");

        model.addAttribute("projectQuoteList", quoteService.getQuoteListByStatus(Status.PROJECT));
        model.addAttribute("cutQuoteList", quoteService.getQuoteListByStatus(Status.CUT));
        model.addAttribute("productionQuoteList", quoteService.getQuoteListByStatus(Status.PRODUCTION));
        model.addAttribute("buildQuoteList", quoteService.getQuoteListByStatus(Status.BUILD));
        model.addAttribute("deliveryQuoteList", quoteService.getQuoteListByStatus(Status.DELIVERY));

        return modelAndView;
    }
}
