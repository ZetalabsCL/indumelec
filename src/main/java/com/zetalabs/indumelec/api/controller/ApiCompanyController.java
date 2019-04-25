package com.zetalabs.indumelec.api.controller;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiCompanyController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/api/company/search")
    public Company companySearch(@RequestParam("taxId") String taxId) {
        return companyService.getCompanyByTaxId(taxId);
    }

    @RequestMapping("/api/company/list")
    public Map<String, Object> companyList() {
        Map<String, Object> result = new HashMap<>();
        List<Company> companyList = companyService.getCompanyList();

        result.put("data", companyList);
        result.put("draw", 1);
        result.put("recordsTotal", companyList.size());
        result.put("recordsFiltered", companyList.size());

        return result;
    }
}