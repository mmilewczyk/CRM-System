package com.agiklo.oracledatabase.security;

import com.agiklo.oracledatabase.service.EmployeeService;
import com.agiklo.oracledatabase.security.jwt.JwtConfiguration;
import com.agiklo.oracledatabase.security.jwt.JwtUsernameAndPasswordAuthorizationFilter;
import com.agiklo.oracledatabase.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final EmployeeService employeeService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SecretKey secretKey;
    private final JwtConfiguration jwtConfiguration;

    @Override
    protected void configure(HttpSecurity http)
            throws Exception {
        http
                .csrf().disable() //disable only for coding process
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration, secretKey))
                .addFilterAfter(new JwtUsernameAndPasswordAuthorizationFilter(secretKey, jwtConfiguration), JwtUsernameAndPasswordAuthenticationFilter.class);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(employeeService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
}
