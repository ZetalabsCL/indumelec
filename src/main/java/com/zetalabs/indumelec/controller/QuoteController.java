package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.QuoteDetail;
import com.zetalabs.indumelec.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;

@Controller
public class QuoteController {
    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value={"/shared/quote/new"}, method = RequestMethod.GET)
    public ModelAndView newquote(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shared/newquote");

        Quote quote = new Quote();
        quote.setQuoteDetails(new HashSet<>());
        quote.setQuoteHistories(new HashSet<>());
        quote.setCompany(new Company());

        model.addAttribute("quote", quote);
        model.addAttribute("quoteDetail", new QuoteDetail());

        return modelAndView;
    }

    @RequestMapping(value={"/shared/quote/show"}, method = RequestMethod.GET)
    public ModelAndView showquotes(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shared/showquotes");

        model.addAttribute("quoteList", quoteService.getQuoteList());
        model.addAttribute("quotesInformation", quoteService.getQuotesInformation());

        return modelAndView;
    }

    @RequestMapping(params = "add", value={"/shared/quote/save"}, method = RequestMethod.POST)
    public ModelAndView add(Model model, Quote quote, QuoteDetail quoteDetail){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shared/newquote");

        if (quote.getQuoteDetails()==null){
            quote.setQuoteDetails(new HashSet<>());
        }

        quoteDetail.setOrderId(quote.getQuoteDetails().size() + 1);
        quote.getQuoteDetails().add(quoteDetail);

        model.addAttribute("quote", quote);
        model.addAttribute("quoteDetail", new QuoteDetail());

        return modelAndView;
    }

    @RequestMapping(params = "save", value={"/shared/quote/save"}, method = RequestMethod.POST)
    public ModelAndView save(Model model, Quote quote){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shared/showquotes");

        model.addAttribute("quoteList", quoteService.getQuoteList());
        model.addAttribute("quotesInformation", quoteService.getQuotesInformation());

        return modelAndView;
    }
}