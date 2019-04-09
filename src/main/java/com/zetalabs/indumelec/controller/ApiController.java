package com.zetalabs.indumelec.controller;

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
public class ApiController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/api/company/search")
    public Company companySearch(@RequestParam("taxId") String taxId) {
        return companyService.getCompanyByTaxId(taxId);
    }

    @RequestMapping("/api/company/list")
    public Map<String, List<Company>> companyList() {
        Map<String, List<Company>> result = new HashMap<>();
        result.put("data", companyService.getCompanyList());

        return result;
    }
}
