package com.zetalabs.indumelec.controller.admin;

import com.zetalabs.indumelec.model.User;
import com.zetalabs.indumelec.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller("userMaintainerController")
public class UserController {
    @Autowired
    private UserService userService;

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

        User user = userService.getUserById(id);

        setDefaultModel(loggedUser, model, user);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/user/delete/{id}"}, method = RequestMethod.GET)
    public ModelAndView delete(HttpSession session, Model model, @PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/user");

        User loggedUser = (User) session.getAttribute("user");

        userService.delete(id);

        setDefaultModel(loggedUser, model);

        return modelAndView;
    }

    @RequestMapping(value={"/admin/user/save"}, method = RequestMethod.POST)
    public ModelAndView save(HttpSession session, Model model, User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/user");

        User loggedUser = (User) session.getAttribute("user");

        userService.save(loggedUser, user);

        setDefaultModel(loggedUser, model);

        return modelAndView;
    }

    private void setDefaultModel(User loggedUser, Model model, User user){
        model.addAttribute("userList", userService.getUserList(loggedUser));
        model.addAttribute("newUser", user);
    }

    private void setDefaultModel(User loggedUser, Model model){
        setDefaultModel(loggedUser, model, new User());
    }
}
