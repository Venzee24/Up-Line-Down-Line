package com.cyberz.ar7demon.controller;

import com.cyberz.ar7demon.dto.auth.SignInDto;
import com.cyberz.ar7demon.dto.auth.SignInResponseDto;
import com.cyberz.ar7demon.dto.auth.SignUpDto;
import com.cyberz.ar7demon.exception.CustomException;
import com.cyberz.ar7demon.model.entity.*;
import com.cyberz.ar7demon.repository.*;
import com.cyberz.ar7demon.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SeniorMasterRepository seniorMasterRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private  JWTService jwtService;
    @Autowired
    private UserDetailServiceForUser userDetailServiceForUser;
    @Autowired
    private UserDetailServiceForAdmin userDetailServiceForAdmin;
    @Autowired
    private UserDetailServiceForMaster userDetailServiceForMaster;
    @Autowired
    private UserDetailServiceForAgent userDetailServiceForAgent;
    @Autowired
    private UserDetailServiceForSeniorMaster userDetailServiceForSeniorMaster;
    @Autowired
    private  TokenRepository tokenRepository;
    @Autowired
    private MasterRepository masterRepository;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto){
        if (userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already existed.", HttpStatus.BAD_REQUEST);

        }
        User user = new User();
        user.setName(signUpDto.getFirstName()+" "+ signUpDto.getLastName());
        user.setEmail(signUpDto.getEmail());
        user.setPhone(signUpDto.getPhone());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        userRepository.save(user);
        return new ResponseEntity<>("Register Success.",HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponseDto> login(@RequestBody SignInDto signInDto){
        User user = userRepository.findByEmail(signInDto.getEmail()).orElseThrow(()
        ->new CustomException("Email not found!"));


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail(),signInDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailServiceForUser.loadUserByUsername(signInDto.getEmail());
        String token = jwtService.generateToken(userDetails);
        var testToken = tokenRepository.findByUser(user);
        if (testToken.isEmpty()) {
            AuthenticationToken authenticationToken = new AuthenticationToken();
            authenticationToken.setToken(token);
            authenticationToken.setUser(user);
            authenticationToken.setCreatedDate(new Date());
            tokenRepository.save(authenticationToken);
        }
        String refreshedToken = jwtService.refreshedToken(token);
        SignInResponseDto signInResponseDto = new SignInResponseDto();
        signInResponseDto.setToken(token);
        signInResponseDto.setRefreshedToken(refreshedToken);
        return new ResponseEntity<>(signInResponseDto,HttpStatus.OK);
    }
    @PostMapping("/adminLogin")
    public ResponseEntity<SignInResponseDto> adminLogin(@RequestBody SignInDto signInDto){
        Admin dbAdmin = adminRepository.findByName("admin");
        if (null==dbAdmin) {
            Admin admin = new Admin();
            admin.setEmail("admin@gmail.com");
            admin.setName("admin");
            admin.setPassword(passwordEncoder.encode("admin12345"));
            admin.setRole(Role.ADMIN);
            admin.setUnit(1000000l);
            adminRepository.save(admin);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail(),signInDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailServiceForAdmin.loadUserByUsername(signInDto.getEmail());
        String token = jwtService.generateToken(userDetails);
        var testToken = tokenRepository.findByAdmin(dbAdmin);
        if (testToken.isEmpty()) {
            AuthenticationToken authenticationToken = new AuthenticationToken();
            authenticationToken.setToken(token);
            authenticationToken.setAdmin(dbAdmin);
            authenticationToken.setCreatedDate(new Date());
            tokenRepository.save(authenticationToken);
        }
        String refreshedToken = jwtService.refreshedToken(token);
        SignInResponseDto signInResponseDto = new SignInResponseDto();
        signInResponseDto.setToken(token);
        signInResponseDto.setRefreshedToken(refreshedToken);
        return new ResponseEntity<>(signInResponseDto,HttpStatus.OK);
    }
    @PostMapping("/seniorMasterLogin")
    public ResponseEntity<SignInResponseDto> seniorMasterLogin(@RequestBody SignInDto signInDto){
        SeniorMaster seniorMaster = seniorMasterRepository.findByEmail(signInDto.getEmail()).orElseThrow(()
                ->new CustomException("Email not found!"));


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail(),signInDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailServiceForSeniorMaster.loadUserByUsername(signInDto.getEmail());
        String token = jwtService.generateToken(userDetails);
        var testToken = tokenRepository.findBySeniorMaster(seniorMaster);
        if (testToken.isEmpty()) {
            AuthenticationToken authenticationToken = new AuthenticationToken();
            authenticationToken.setToken(token);
            authenticationToken.setSeniorMaster(seniorMaster);
            authenticationToken.setCreatedDate(new Date());
            tokenRepository.save(authenticationToken);
        }
        String refreshedToken = jwtService.refreshedToken(token);
        SignInResponseDto signInResponseDto = new SignInResponseDto();
        signInResponseDto.setToken(token);
        signInResponseDto.setRefreshedToken(refreshedToken);
        return new ResponseEntity<>(signInResponseDto,HttpStatus.OK);
    }
    @PostMapping("/masterLogin")
    public ResponseEntity<SignInResponseDto> masterLogin(@RequestBody SignInDto signInDto){
        Master master = masterRepository.findByEmail(signInDto.getEmail()).orElseThrow(()
                ->new CustomException("Email not found!"));


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail(),signInDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailServiceForMaster.loadUserByUsername(signInDto.getEmail());
        String token = jwtService.generateToken(userDetails);
        var testToken = tokenRepository.findByMaster(master);
        if (testToken.isEmpty()) {
            AuthenticationToken authenticationToken = new AuthenticationToken();
            authenticationToken.setToken(token);
            authenticationToken.setMaster(master);
            authenticationToken.setCreatedDate(new Date());
            tokenRepository.save(authenticationToken);
        }
        String refreshedToken = jwtService.refreshedToken(token);
        SignInResponseDto signInResponseDto = new SignInResponseDto();
        signInResponseDto.setToken(token);
        signInResponseDto.setRefreshedToken(refreshedToken);
        return new ResponseEntity<>(signInResponseDto,HttpStatus.OK);
    }
    @PostMapping("/agentLogin")
    public ResponseEntity<SignInResponseDto> agentLogin(@RequestBody SignInDto signInDto){
        Agent agent = agentRepository.findByEmail(signInDto.getEmail()).orElseThrow(()
                ->new CustomException("Email not found!"));


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail(),signInDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailServiceForAgent.loadUserByUsername(signInDto.getEmail());
        String token = jwtService.generateToken(userDetails);
        var testToken = tokenRepository.findByAgent(agent);
        if (testToken.isEmpty()) {
            AuthenticationToken authenticationToken = new AuthenticationToken();
            authenticationToken.setToken(token);
            authenticationToken.setAgent(agent);
            authenticationToken.setCreatedDate(new Date());
            tokenRepository.save(authenticationToken);
        }
        String refreshedToken = jwtService.refreshedToken(token);
        SignInResponseDto signInResponseDto = new SignInResponseDto();
        signInResponseDto.setToken(token);
        signInResponseDto.setRefreshedToken(refreshedToken);
        return new ResponseEntity<>(signInResponseDto,HttpStatus.OK);
    }
}
