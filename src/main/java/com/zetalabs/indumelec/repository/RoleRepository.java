package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByRoleId(Integer id);
}
