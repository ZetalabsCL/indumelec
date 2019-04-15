package com.zetalabs.indumelec.service;

import com.zetalabs.indumelec.model.Role;
import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.repository.RoleRepository;
import com.zetalabs.indumelec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> getUserList(User loggedUser){
        return  userRepository.findUsersByUserIdNotIn(loggedUser.getUserId());
    }

    public User getUserById(Long userId){
        User user = userRepository.findByUserId(userId);
        user.setPassword(null);

        if (user.getRoles()!=null && !user.getRoles().isEmpty()){
            Integer roleId = ((Role)(user.getRoles().toArray()[0])).getRoleId();
            user.setRole(roleId);
        }

        return user;
    }

    public User getUserByMail(String mail){
        return userRepository.findByEmail(mail);
    }

    public void delete(Long userId){
        userRepository.deleteById(userId);
    }

    public void save(User loggedUser, User user){
        String encryptedPwd = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);

        Role role = roleRepository.getByRoleId(user.getRole());

        Set<Role> roleList = new HashSet<>();
        roleList.add(role);

        user.setRoles(roleList);
        user.setActive("Y");
        user.setUpdatedBy(loggedUser.getEmail());
        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);
    }
}