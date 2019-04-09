package com.zetalabs.indumelec.controller.admin;

import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.model.types.Status;
import com.zetalabs.indumelec.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class AdminController {
    @Autowired
    private QuoteService quoteService;

    @RequestMapping(value={"/admin/dashboard"}, method = RequestMethod.GET)
    public ModelAndView index(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/dashboard");

        setDefaultModel(model);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/dashboard/approve/{id}"}, method = RequestMethod.GET)
    public ModelAndView approve(HttpSession session, Model model, @PathVariable(name = "id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/dashboard");

        User loggedUser = (User) session.getAttribute("user");

        quoteService.approveQuote(loggedUser, id);

        setDefaultModel(model);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/dashboard/reject/{id}"}, method = RequestMethod.GET)
    public ModelAndView reject(HttpSession session, Model model, @PathVariable(name = "id") Long id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/dashboard");

        User loggedUser = (User) session.getAttribute("user");

        quoteService.rejectQuote(loggedUser, id);

        setDefaultModel(model);

        return modelAndView;
    }

    private void setDefaultModel(Model model){
        model.addAttribute("quoteList", quoteService.getQuoteListByStatus(Status.REVIEW));
    }
}