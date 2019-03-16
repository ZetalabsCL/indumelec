package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByCompanyId(Long id);
}
