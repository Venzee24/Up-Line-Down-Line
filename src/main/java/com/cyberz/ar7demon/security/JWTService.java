package com.cyberz.ar7demon.security;


import com.cyberz.ar7demon.exception.CustomException;
import com.cyberz.ar7demon.model.entity.*;
import com.cyberz.ar7demon.repository.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class JWTService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private SeniorMasterRepository seniorMasterRepository;
    public String refreshedToken(String token){
        String email = getEmailFromJWT(token);
        String newToken= Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.JWT_SECRET) // Consider stronger algorithm (HS512 or higher)
                .compact();
        return newToken;
    }
    public String generateToken(UserDetails userDetails){
        String email = userDetails.getUsername();
        System.out.println(email);
        Date currentDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis()+ SecurityConstants.JWT_EXPIRATION*24*60*60*1000);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,SecurityConstants.JWT_SECRET)
                .compact();


        return token;
    }
    public String getEmailFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("Token is expire or incorrect");
        }
    }
    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }

    public User getUserFromToken(String token) {
        String email = getEmailFromJWT(token);
        User user = userRepository.findByEmail(email).get();
        return user;
    }
    public Agent getAgentFromToken(String token) {
        String email = getEmailFromJWT(token);
        Agent agent = agentRepository.findByEmail(email).get();
        return agent;
    }
    public Master getMasterFromToken(String token) {
        String email = getEmailFromJWT(token);
        Master master = masterRepository.findByEmail(email).get();
        return master;
    }
    public SeniorMaster getSeniorMasterFromToken(String token) {
        String email = getEmailFromJWT(token);
        SeniorMaster seniorMaster = seniorMasterRepository.findByEmail(email).get();
        return seniorMaster;
    }
    public Admin getAdminFromToken(String token) {
        String email = getEmailFromJWT(token);
        System.out.println(email);
        Admin admin = adminRepository.findByEmail(email).orElseThrow(()->
                new CustomException("Email not found!"));
        return admin;
    }
}
