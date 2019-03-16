package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CompanyMaintainerController {
    @Autowired
    private CompanyRepository companyRepository;

    @RequestMapping(value={"/admin/company"}, method = RequestMethod.GET)
    public ModelAndView index(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/company");
        model.addAttribute("companyList", companyRepository.findAll());
        model.addAttribute("company", new Company());

        return modelAndView;
    }

    @RequestMapping(value={"/admin/company/edit/{id}"}, method = RequestMethod.GET)
    public ModelAndView companyEditForm(Model model, @PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/company");

        model.addAttribute("companyList", companyRepository.findAll());
        model.addAttribute("company", companyRepository.findByCompanyId(id));

        return modelAndView;
    }

    @RequestMapping(value={"/admin/company/save"}, method = RequestMethod.POST)
    public ModelAndView companyEditForm(Model model, Company company) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/company");

        model.addAttribute("company", companyRepository.save(company));
        model.addAttribute("companyList", companyRepository.findAll());

        return modelAndView;
    }
}
