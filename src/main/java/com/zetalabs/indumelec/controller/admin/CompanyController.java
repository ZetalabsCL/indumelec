package com.zetalabs.indumelec.controller.admin;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping(value={"/admin/company"}, method = RequestMethod.GET)
    public ModelAndView index(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/company");

        setDefaultModel(model);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/company/edit/{id}"}, method = RequestMethod.GET)
    public ModelAndView edit(Model model, @PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/company");

        Company company = companyService.getCompanyById(id);

        setDefaultModel(model, company);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/company/delete/{id}"}, method = RequestMethod.GET)
    public ModelAndView delete(Model model, @PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/company");

        companyService.delete(id);

        setDefaultModel(model);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/company/save"}, method = RequestMethod.POST)
    public ModelAndView save(Model model, Company company) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/company");

        companyService.save(company);

        setDefaultModel(model);

        return modelAndView;
    }

    private void setDefaultModel(Model model, Company company){
        model.addAttribute("companyList", companyService.getCompanyList());
        model.addAttribute("company", company);
    }

    private void setDefaultModel(Model model){
        setDefaultModel(model, new Company());
    }
}