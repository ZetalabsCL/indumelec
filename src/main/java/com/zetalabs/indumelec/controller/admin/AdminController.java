package com.zetalabs.indumelec.controller.admin;

import com.zetalabs.indumelec.model.types.QuoteStatus;
import com.zetalabs.indumelec.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private QuoteRepository quoteRepository;

    @RequestMapping(value={"/admin/dashboard"}, method = RequestMethod.GET)
    public ModelAndView dashboard(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/dashboard");

        model.addAttribute("quoteList", quoteRepository.getQuotesNotIn(notValidStatus()));
        return modelAndView;
    }

    private List<Integer> notValidStatus(){
        List<Integer> statusList = new ArrayList<>();

        statusList.add(QuoteStatus.REJECTED.getStatusId());
        statusList.add(QuoteStatus.COMPLETED.getStatusId());

        return statusList;
    }
}
