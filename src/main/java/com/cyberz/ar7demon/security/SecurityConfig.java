package com.cyberz.ar7demon.security;

import com.cyberz.ar7demon.model.entity.Role;

import com.cyberz.ar7demon.repository.SeniorMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailServiceForUser userDetailsService;
    @Autowired
    private UserDetailServiceForAdmin userDetailServiceForAdmin;
    @Autowired
    private UserDetailServiceForSeniorMaster userDetailServiceForSeniorMaster;
    @Autowired
    private UserDetailServiceForMaster userDetailServiceForMaster;
    @Autowired
    private UserDetailServiceForAgent userDetailServiceForAgent;
    @Autowired
    private  JwtAuthEntryPoint jwtAuthEntryPoint;
    @Autowired
    private UserDetailServiceForUser userDetailServiceForUser;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
       httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/api/auth/**")
                        .permitAll()
                        .requestMatchers("/api/admin/**").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/user/**").hasAnyAuthority(Role.USER.name())
                                .requestMatchers("api/agent/**").hasAnyAuthority(Role.AGENT.name())
                                .requestMatchers("api/master/**").hasAnyAuthority(Role.MASTER.name())
                                .requestMatchers("api/seniorMaster/**").hasAnyAuthority(Role.SENIOR_MASTER.name())
                        .anyRequest().authenticated()
                        )
               .exceptionHandling(handler->handler.authenticationEntryPoint(jwtAuthEntryPoint))
               .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
       httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
       return httpSecurity.build();



    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }


    @Bean
    AuthenticationManager configure(HttpSecurity http,PasswordEncoder passwordEncoder) throws Exception {
        var builder =http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.authenticationProvider(getAdminProvider(passwordEncoder));

        builder.authenticationProvider(getUserProvider(passwordEncoder));
        builder.authenticationProvider(getSeniorProvider(passwordEncoder));
        builder.authenticationProvider(getAgentProvider(passwordEncoder));
        builder.authenticationProvider(getMasterProvider(passwordEncoder));
        return builder.build();
    }



    private AuthenticationProvider getUserProvider(PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailServiceForUser);
        return provider;
    }

    private AuthenticationProvider getAdminProvider(PasswordEncoder passwordEncoder){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(userDetailServiceForAdmin);
        return provider;
    }
    private AuthenticationProvider getSeniorProvider(PasswordEncoder passwordEncoder){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(userDetailServiceForSeniorMaster);
        return provider;
    }
    private AuthenticationProvider getMasterProvider(PasswordEncoder passwordEncoder){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(userDetailServiceForMaster);
        return provider;
    }
    private AuthenticationProvider getAgentProvider(PasswordEncoder passwordEncoder){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(userDetailServiceForAgent);
        return provider;
    }


}
