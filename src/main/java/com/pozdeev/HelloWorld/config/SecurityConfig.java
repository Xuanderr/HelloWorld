package com.pozdeev.HelloWorld.config;

import com.pozdeev.HelloWorld.models.security.Permission;
import com.pozdeev.HelloWorld.security.JwtTokenPersistenceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;


@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenPersistenceFilter jwtTokenPersistenceFilter;

    @Autowired
    public SecurityConfig(@Qualifier("UserDetailsServiceImpl") UserDetailsService userDetailsService,
                          JwtTokenPersistenceFilter jwtTokenPersistenceFilter)
    {
        this.userDetailsService = userDetailsService;
        this.jwtTokenPersistenceFilter = jwtTokenPersistenceFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.GET,"/users/**").hasAuthority(Permission.USERS_READ.getPermission())
                .antMatchers(HttpMethod.POST, "/users/**").hasAuthority(Permission.USERS_CREATE.getPermission())
                .antMatchers(HttpMethod.PUT, "/users/**").hasAuthority(Permission.USERS_UPDATE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority(Permission.USERS_DELETE.getPermission())
                .antMatchers("/refresh").hasAuthority(Permission.TOKEN_REFRESH.getPermission())
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtTokenPersistenceFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}


