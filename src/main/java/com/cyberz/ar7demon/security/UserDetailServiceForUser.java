package com.cyberz.ar7demon.security;

import com.cyberz.ar7demon.model.entity.Admin;
import com.cyberz.ar7demon.model.entity.Role;
import com.cyberz.ar7demon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class UserDetailServiceForUser implements UserDetailsService {
    @Autowired
    private  UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(customer ->
                        User.withUsername(username)
                                .authorities(Role.USER.name())
                                .password(customer.getPassword())
                                .accountExpired(isExpired(customer))
                                .accountLocked(customer.isLocked())
                                .disabled(customer.isActivated())
                                .credentialsExpired(isCredentialExpired(customer))
                                .build())
                .orElseThrow(()-> new UsernameNotFoundException(username));
    }
    private boolean isCredentialExpired(com.cyberz.ar7demon.model.entity.User customer){
        if (null !=customer.getValidPassDate()){
            LocalDateTime validPassDate = customer.getValidPassDate();
            if (validPassDate.isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }

    private boolean  isExpired(com.cyberz.ar7demon.model.entity.User customer){
        if (null != customer.getRetiredDate()){
            LocalDateTime retiredDate = customer.getRetiredDate();
            if (retiredDate.isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }
}
