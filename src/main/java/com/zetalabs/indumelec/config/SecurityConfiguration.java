package com.zetalabs.indumelec.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

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

    @Bean
    public UserDetailsService jdbcUserDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(usersQuery);
        manager.setAuthoritiesByUsernameQuery(rolesQuery);
        return manager;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/", "/login", "/logoutInfo", "/sessionExpired").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/dashboard").hasAnyAuthority("Admin","User")
                        .requestMatchers("/accessDenied").hasAnyAuthority("Admin","User")
                        .requestMatchers("/quote/**").hasAnyAuthority("Admin","User")
                        .requestMatchers("/admin/**").hasAuthority("Admin")
                        .requestMatchers("/user/**").hasAuthority("User")
                        .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                        .anyRequest().authenticated());

        http.csrf()
                .disable()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .successHandler(indumelecAuthenticationHandler)
                .usernameParameter("email")
                .passwordParameter("password");

        http.logout((logout) -> logout
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/logoutInfo"));

        http.exceptionHandling((exception) -> exception
                        .accessDeniedPage("/accessDenied"));

        http.sessionManagement((sessionManagement) -> sessionManagement
                .sessionFixation().migrateSession()
                .invalidSessionUrl("/invalidSession")
                .maximumSessions(1)
                .expiredUrl("/sessionExpired"));

        return http.build();
    }
}