package com.zetalabs.indumelec.config;

import com.zetalabs.indumelec.model.AppRole;
import com.zetalabs.indumelec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Component
public class IndumelecAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, Authentication authentication)
            throws RuntimeException
    {
        HttpSession session = httpServletRequest.getSession();
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute("username", authUser.getUsername());
        com.zetalabs.indumelec.model.User user = userRepository.findByEmail(authUser.getUsername());
        session.setAttribute("user", user);

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority ->
        {
            if(authority.getAuthority().equals("Admin"))
            {
                session.setAttribute("role", AppRole.ADMIN);
                try {
                    httpServletResponse.sendRedirect("/admin/dashboard");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (authority.getAuthority().equals("User")) {
                session.setAttribute("role", AppRole.USER);

                try {
                    httpServletResponse.sendRedirect("/user/dashboard");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}