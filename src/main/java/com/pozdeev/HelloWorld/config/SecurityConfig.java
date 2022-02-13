package com.pozdeev.HelloWorld.config;

import com.pozdeev.HelloWorld.models.security.Permission;
import com.pozdeev.HelloWorld.security.JwtLogoutSuccessHandler;
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
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;


@EnableWebSecurity
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
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/refresh").hasAuthority(Permission.TOKEN_REFRESH.getPermission())

                .antMatchers(HttpMethod.GET, "/posts/**").permitAll()
                //.antMatchers("/posts/**").authenticated()


                .antMatchers(HttpMethod.PUT, "/comments/**").hasAuthority(Permission.COMMENTS_UPDATE.getPermission())
                .antMatchers(HttpMethod.GET, "/comments/**").permitAll()
                //.antMatchers("/comments/**").authenticated()

                .antMatchers(HttpMethod.GET,"/users/**").hasAuthority(Permission.USERS_READ.getPermission())
                .antMatchers(HttpMethod.PUT, "/users/properties/**").hasAuthority(Permission.USERS_PROPERTIES_UPDATE.getPermission())
                .antMatchers(HttpMethod.POST, "/users/**").permitAll()
                .antMatchers("/users/**").hasAnyAuthority(Permission.USERS_UPDATE.getPermission(), Permission.USERS_DELETE.getPermission())

                .antMatchers(HttpMethod.GET, "/tags/**").permitAll()
                .antMatchers("/tags/**").hasAnyAuthority(Permission.TAGS_CREATE.getPermission(), Permission.TAGS_UPDATE.getPermission(), Permission.TAGS_DELETE.getPermission())

                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtTokenPersistenceFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().logoutSuccessHandler(logoutSuccessHandler());
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new JwtLogoutSuccessHandler();
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


