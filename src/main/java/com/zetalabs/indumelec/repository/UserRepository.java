package com.zetalabs.indumelec.repository;

import com.zetalabs.indumelec.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUserId(Long id);
    List<User> findUsersByUserIdNotIn(List<Long> id);
}
