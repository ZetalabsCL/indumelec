package com.zetalabs.indumelec.controller.admin;

import com.zetalabs.indumelec.api.dto.ProductionWrapper;
import com.zetalabs.indumelec.model.types.PriorityType;
import com.zetalabs.indumelec.model.types.Status;
import com.zetalabs.indumelec.service.QuoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductionController {
    private final QuoteService quoteService;

    public ProductionController(QuoteService quoteService){
        this.quoteService = quoteService;
    }

    @RequestMapping(value={"/admin/production"}, method = RequestMethod.GET)
    public ModelAndView dashboard(Model model,
                                  @RequestParam(value = "priorityType", required = false) String priorityType,
                                  @RequestParam(value = "workOrderFilter", required = false) String workOrderFilter){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/production");

        model.addAttribute("workOrderFilter", workOrderFilter);
        if (StringUtils.isNotEmpty(priorityType) && !"ALL".equals(priorityType)) {
            model.addAttribute("priorityType", PriorityType.valueOf(priorityType));
            model.addAttribute("quotesList", getQuoteDetailsByPriority(PriorityType.valueOf(priorityType), workOrderFilter));
        } else {
            model.addAttribute("quotesList", getQuoteDetails(workOrderFilter));
        }

        return modelAndView;
    }

    private List<ProductionWrapper> getQuoteDetails(String workOrder){
        List<ProductionWrapper> resultList = new ArrayList<>();

        resultList.add(new ProductionWrapper(Status.PROJECT, quoteService.getQuoteListByStatus(Status.PROJECT, workOrder)));
        resultList.add(new ProductionWrapper(Status.CUT, quoteService.getQuoteListByStatus(Status.CUT, workOrder)));
        resultList.add(new ProductionWrapper(Status.PRODUCTION, quoteService.getQuoteListByStatus(Status.PRODUCTION, workOrder)));
        resultList.add(new ProductionWrapper(Status.BUILD, quoteService.getQuoteListByStatus(Status.BUILD, workOrder)));
        resultList.add(new ProductionWrapper(Status.DELIVERY, quoteService.getQuoteListByStatus(Status.DELIVERY, workOrder)));

        return resultList;
    }

    private List<ProductionWrapper> getQuoteDetailsByPriority(PriorityType priorityType, String workOrder){
        List<ProductionWrapper> resultList = new ArrayList<>();

        resultList.add(new ProductionWrapper(Status.PROJECT,
                quoteService.getQuoteListByStatusAndPriorityType(Status.PROJECT, priorityType, workOrder)));
        resultList.add(new ProductionWrapper(Status.CUT,
                quoteService.getQuoteListByStatusAndPriorityType(Status.CUT, priorityType, workOrder)));
        resultList.add(new ProductionWrapper(Status.PRODUCTION,
                quoteService.getQuoteListByStatusAndPriorityType(Status.PRODUCTION, priorityType, workOrder)));
        resultList.add(new ProductionWrapper(Status.BUILD,
                quoteService.getQuoteListByStatusAndPriorityType(Status.BUILD, priorityType, workOrder)));
        resultList.add(new ProductionWrapper(Status.DELIVERY,
                quoteService.getQuoteListByStatusAndPriorityType(Status.DELIVERY, priorityType, workOrder)));

        return resultList;
    }
}
