package com.cyberz.ar7demon.security;

import com.cyberz.ar7demon.model.entity.Admin;
import com.cyberz.ar7demon.model.entity.Role;
import com.cyberz.ar7demon.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
@Service
public class UserDetailServiceForAdmin implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByEmail(username)
                .map(customer ->
                        User.withUsername(username)
                                .authorities(Role.ADMIN.name())
                                .password(customer.getPassword())
                                .accountExpired(isExpired(customer))
                                .accountLocked(customer.isLocked())
                                .disabled(customer.isActivated())
                                .credentialsExpired(isCredentialExpired(customer))
                                .build())
                .orElseThrow(()-> new UsernameNotFoundException(username));
    }
    private boolean isCredentialExpired(Admin customer){
        if (null !=customer.getValidPassDate()){
            LocalDateTime validPassDate = customer.getValidPassDate();
            if (validPassDate.isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }

    private boolean  isExpired(Admin customer){
        if (null != customer.getRetiredDate()){
            LocalDateTime retiredDate = customer.getRetiredDate();
            if (retiredDate.isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }
}
