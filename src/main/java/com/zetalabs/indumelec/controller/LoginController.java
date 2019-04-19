package com.zetalabs.indumelec.controller;

import com.zetalabs.indumelec.model.types.AppRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value={"/logoutInfo"}, method = RequestMethod.GET)
    public ModelAndView logout(){
        SecurityContextHolder.getContext().setAuthentication(null);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("logout");
        return modelAndView;
    }

    @RequestMapping(value={"/accessDenied"}, method = RequestMethod.GET)
    public ModelAndView accessDenied(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("access-denied");
        return modelAndView;
    }

    @RequestMapping(value={"/sessionExpired"}, method = RequestMethod.GET)
    public ModelAndView sessionExpired(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("session-expired");
        return modelAndView;
    }

    @RequestMapping(value={"/dashboard"}, method = RequestMethod.GET)
    public String save(HttpSession session){
        Object role = session.getAttribute("role");
        String destination="redirect:/user/dashboard";

        if (AppRole.ADMIN.equals(role)){
            destination = "redirect:/admin/dashboard";
        }

        return destination;
    }
}
