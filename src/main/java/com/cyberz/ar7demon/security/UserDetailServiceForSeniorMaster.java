package com.cyberz.ar7demon.security;

import com.cyberz.ar7demon.model.entity.Role;
import com.cyberz.ar7demon.model.entity.SeniorMaster;
import com.cyberz.ar7demon.repository.SeniorMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class UserDetailServiceForSeniorMaster implements UserDetailsService {
    @Autowired
    private SeniorMasterRepository seniorMasterRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return seniorMasterRepository.findByEmail(username)
                .map(customer ->
                        User.withUsername(username)
                                .authorities(Role.SENIOR_MASTER.name())
                                .password(customer.getPassword())
                                .accountExpired(isExpired(customer))
                                .accountLocked(customer.isLocked())
                                .disabled(customer.isActivated())
                                .credentialsExpired(isCredentialExpired(customer))
                                .build())
                .orElseThrow(()-> new UsernameNotFoundException(username));
    }
    private boolean isCredentialExpired(SeniorMaster customer){
        if (null !=customer.getValidPassDate()){
            LocalDateTime validPassDate = customer.getValidPassDate();
            if (validPassDate.isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }

    private boolean  isExpired(SeniorMaster customer){
        if (null != customer.getRetiredDate()){
            LocalDateTime retiredDate = customer.getRetiredDate();
            if (retiredDate.isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }
}
