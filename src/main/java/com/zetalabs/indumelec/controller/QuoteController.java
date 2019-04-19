package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.model.Quote;
import com.zetalabs.indumelec.model.QuoteDetail;
import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.model.types.AppRole;
import com.zetalabs.indumelec.service.PdfGenerator;
import com.zetalabs.indumelec.service.QuoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

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
    public ModelAndView newquote(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("quote/new");

        Quote quote = new Quote();
        quote.setQuoteDetailsList(new LinkedList<>());
        quote.setCompany(new Company());
        quote.setQuoteDetail(new QuoteDetail());
        quote.setDeliveryDate(LocalDate.now());

        model.addAttribute("quote", quote);

        return modelAndView;
    }

    @RequestMapping(params = "add", value={"/quote/save"}, method = RequestMethod.POST)
    public ModelAndView add(Quote quote){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("quote/new");

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

    @RequestMapping(params = "save", value={"/quote/save"}, method = RequestMethod.POST)
    public String save(HttpSession session, Model model, Quote quote){
        User loggedUser = (User) session.getAttribute("user");
        Object role = session.getAttribute("role");
        String destination="redirect:/user/dashboard";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        quote.setDeliveryDate(LocalDate.parse(quote.getDeliveryDateStr(), formatter));

        quoteService.saveQuote(loggedUser, quote);

        if (AppRole.ADMIN.equals(role)){
            destination = "redirect:/admin/dashboard";
        }

        return destination;
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