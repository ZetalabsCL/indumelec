package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class QuoteController {
    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value={"/quote/list"}, method = RequestMethod.GET)
    public ModelAndView dashboard(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("quote/list");

        return modelAndView;
    }

    @RequestMapping(value={"/quote/new"}, method = RequestMethod.GET)
    public ModelAndView newquote(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("quote/new");

        return modelAndView;
    }

    @RequestMapping(value={"/quote/downloadQuotePdf"}, method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getQuotePdf(Long quoteId){
        Quote quote = quoteService.getQuoteById(quoteId);
        byte[] data = quoteService.getQuotePdf(quote);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+quote.getQuoteCode()+".pdf\"" )
                .contentType(MediaType.APPLICATION_PDF).contentLength(data.length)
                .body(resource);
    }
}