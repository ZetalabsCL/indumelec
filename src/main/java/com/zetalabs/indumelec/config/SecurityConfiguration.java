package com.zetalabs.indumelec.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    private IndumelecAuthenticationHandler indumelecAuthenticationHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
        .usersByUsernameQuery(usersQuery)
        .authoritiesByUsernameQuery(rolesQuery)
        .dataSource(dataSource)
        .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/login", "/logoutInfo", "/sessionExpired").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/dashboard").hasAnyAuthority("Admin","User")
                .antMatchers("/accessDenied").hasAnyAuthority("Admin","User")
                .antMatchers("/quote/**").hasAnyAuthority("Admin","User")
                .antMatchers("/admin/**").hasAuthority("Admin")
                .antMatchers("/user/**").hasAuthority("User")
                .anyRequest().authenticated();

        http.csrf()
                .disable()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .successHandler(indumelecAuthenticationHandler)
                .usernameParameter("email")
                .passwordParameter("password");

        http.logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/logoutInfo");

        http.exceptionHandling()
                .accessDeniedPage("/accessDenied");

        http.sessionManagement()
                .sessionFixation().migrateSession()
                .invalidSessionUrl("/invalidSession")
                .maximumSessions(1)
                .expiredUrl("/sessionExpired");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**");
    }
}