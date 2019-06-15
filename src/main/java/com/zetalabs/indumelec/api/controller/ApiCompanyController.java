package com.zetalabs.indumelec.api.controller;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.service.CompanyService;
import com.zetalabs.indumelec.utils.FormUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping("/api/company/save")
    public ResponseEntity edit(@RequestParam("frmInfo") String frmInfo) {
        ResponseEntity responseEntity = null;
        Company company = getCompany(frmInfo);

        if (company.getCompanyId()==null && companyService.getCompanyByTaxId(company.getTaxId())!=null) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Empresa ya existe");
        } else {
            responseEntity = ResponseEntity.ok(HttpStatus.OK);
            companyService.save(company);
        }

        return responseEntity;
    }

    @RequestMapping("/api/company/edit")
    public Company edit(@RequestParam(name = "companyId") Long companyId) {
        return companyService.getCompanyById(companyId);
    }

    @RequestMapping("/api/company/delete")
    public ResponseEntity delete(@RequestParam(name = "companyId") Long companyId) {
        ResponseEntity responseEntity = null;

        try {
            responseEntity = ResponseEntity.ok(HttpStatus.OK);
            companyService.delete(companyId);
        } catch (Exception ex) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Empresa utilizada en cotizaciones. No puede ser eliminada");
        }

        return responseEntity;
    }

    private Company getCompany(String frmInfo) {
        Company company = new Company();

        Map<String, String> map = FormUtils.getMap(new JSONArray(frmInfo));
        String companyId = map.get("companyId");

        if (!StringUtils.isEmpty(companyId)) {
            company.setCompanyId(Long.valueOf(companyId));
        }

        company.setTaxId(map.get("taxid"));
        company.setName(map.get("name"));
        company.setAddress(map.get("address"));
        company.setPhone(map.get("phone"));

        return company;
    }
}