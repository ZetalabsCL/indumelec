package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.QuoteDetail;
import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.service.QuoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;

@Controller
public class QuoteController {
    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value={"/shared/quote/new"}, method = RequestMethod.GET)
    public ModelAndView newquote(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shared/newquote");

        Quote quote = new Quote();
        quote.setQuoteDetailsList(new LinkedList<>());
        quote.setCompany(new Company());
        quote.setQuoteDetail(new QuoteDetail());

        model.addAttribute("quote", quote);

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
    public ModelAndView add(Model model, Quote quote){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shared/newquote");

        if (quote.getQuoteDetailsList() == null){
            quote.setQuoteDetailsList(new LinkedList<>());
        }

        QuoteDetail newQuoteDetail = new QuoteDetail();
        BeanUtils.copyProperties(quote.getQuoteDetail(), newQuoteDetail);

        newQuoteDetail.setOrderId(quote.getQuoteDetailsList().size() + 1);
        quote.getQuoteDetailsList().add(newQuoteDetail);

        quote.setQuoteDetail(new QuoteDetail());

        return modelAndView;
    }

    @RequestMapping(params = "save", value={"/shared/quote/save"}, method = RequestMethod.POST)
    public ModelAndView save(HttpSession session, Model model, Quote quote){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("shared/showquotes");

        User loggedUser = (User) session.getAttribute("user");

        quoteService.saveQuote(loggedUser, quote);

        model.addAttribute("quoteList", quoteService.getQuoteList());
        model.addAttribute("quotesInformation", quoteService.getQuotesInformation());

        return modelAndView;
    }
}