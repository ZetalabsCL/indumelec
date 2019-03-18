package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Company;
import com.zetalabs.indumelec.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> getCompanyList(){
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long companyId){
         return companyRepository.findByCompanyId(companyId);
    }

    public void delete(Long companyId){
        companyRepository.deleteById(companyId);
    }

    public void save(Company company){
        companyRepository.save(company);
    }
}
