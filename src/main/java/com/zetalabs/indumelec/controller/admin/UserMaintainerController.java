package com.zetalabs.indumelec.controller.admin;

import com.zetalabs.indumelec.model.Role;
import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.repository.RoleRepository;
import com.zetalabs.indumelec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class UserMaintainerController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value={"/admin/user"}, method = RequestMethod.GET)
    public ModelAndView index(HttpSession session, Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/user");

        User loggedUser = (User) session.getAttribute("user");

        setDefaultModel(loggedUser, model);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/user/edit/{id}"}, method = RequestMethod.GET)
    public ModelAndView edit(HttpSession session, Model model, @PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/user");

        User loggedUser = (User) session.getAttribute("user");

        User user = userRepository.findByUserId(id);
        user.setPassword(null);

        if (user.getRoles()!=null && !user.getRoles().isEmpty()){
            Integer roleId = ((Role)(user.getRoles().toArray()[0])).getRoleId();
            user.setRole(roleId);
        }

        model.addAttribute("userList", userRepository.findUsersByUserIdNotIn(loggedUser.getUserId()));
        model.addAttribute("newUser", user);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/user/delete/{id}"}, method = RequestMethod.GET)
    public ModelAndView delete(HttpSession session, Model model, @PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/user");

        User loggedUser = (User) session.getAttribute("user");

        userRepository.deleteById(id);

        setDefaultModel(loggedUser, model);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/user/save"}, method = RequestMethod.POST)
    public ModelAndView save(HttpSession session, Model model, User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/user");

        String encryptedPwd = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPwd);

        User loggedUser = (User) session.getAttribute("user");

        Role role = roleRepository.getByRoleId(user.getRole());

        Set<Role> roleList = new HashSet<>();
        roleList.add(role);

        user.setRoles(roleList);
        user.setActive("Y");
        user.setUpdatedBy(loggedUser.getEmail());
        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);

        setDefaultModel(loggedUser, model);

        return modelAndView;
    }

    private void setDefaultModel(User user, Model model){
        model.addAttribute("userList", userRepository.findUsersByUserIdNotIn(user.getUserId()));
        model.addAttribute("newUser", new User());
    }
}
