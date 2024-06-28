package com.cyberz.ar7demon.security;

import com.cyberz.ar7demon.model.entity.*;
import com.cyberz.ar7demon.repository.*;
import com.cyberz.ar7demon.service.AdminService;
import com.cyberz.ar7demon.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SeniorMasterRepository seniorMasterRepository;
    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserDetailServiceForUser userDetailServiceForUser;
    @Autowired
    private UserDetailServiceForAdmin userDetailServiceForAdmin;
    @Autowired
    private UserDetailServiceForMaster userDetailServiceForMaster;
    @Autowired
    private UserDetailServiceForSeniorMaster userDetailServiceForSeniorMaster;
    @Autowired
    private UserDetailServiceForAgent userDetailServiceForAgent;
    public JwtAuthenticationFilter(){

    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String token = jwtService.getJWTFromRequest(request);
        System.out.println(token);
      if (StringUtils.hasText(token)&& jwtService.validateToken(token)){
          String email = jwtService.getEmailFromJWT(token);
          Optional<User> user = userRepository.findByEmail(email);
          Optional<Admin> admin = adminRepository.findByEmail(email);
          Optional<SeniorMaster> seniorMaster = seniorMasterRepository.findByEmail(email);
          Optional<Master> master = masterRepository.findByEmail(email);
          Optional<Agent> agent = agentRepository.findByEmail(email);
          if (user.isPresent()) {
              UserDetails userDetails = userDetailServiceForUser.loadUserByUsername(email);
              UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
              usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
          }
          if (admin.isPresent()){
              UserDetails userDetails = userDetailServiceForAdmin.loadUserByUsername(email);
              UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
              usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
          }
          if (seniorMaster.isPresent()){
              UserDetails userDetails = userDetailServiceForSeniorMaster.loadUserByUsername(email);
              UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
              usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
          }
          if (master.isPresent()){
              UserDetails userDetails = userDetailServiceForMaster.loadUserByUsername(email);
              UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
              usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
          }
          if (agent.isPresent()){
              UserDetails userDetails = userDetailServiceForAgent.loadUserByUsername(email);
              UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
              usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
          }

      }
       filterChain.doFilter(request,response);
    }


}
