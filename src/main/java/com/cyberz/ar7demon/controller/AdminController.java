package com.cyberz.ar7demon.controller;

import com.cyberz.ar7demon.dto.requestDto.*;
import com.cyberz.ar7demon.dto.auth.ResponseDto;
import com.cyberz.ar7demon.model.entity.*;
import com.cyberz.ar7demon.security.JWTService;
import com.cyberz.ar7demon.security.UserDetailServiceForAdmin;
import com.cyberz.ar7demon.service.AgentService;
import com.cyberz.ar7demon.service.MasterService;
import com.cyberz.ar7demon.service.SeniorMasterService;
import com.cyberz.ar7demon.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private SeniorMasterService seniorMasterService;
    @Autowired
    private MasterService masterService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserDetailServiceForAdmin userDetailServiceForAdmin;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/createSeniorMaster")
    public ResponseEntity<ResponseDto> createSeniorMaster(@RequestBody CreateEntityRequestDto requestDto, HttpServletRequest request){
        Admin admin = checkAuthorize(request);
        ResponseDto response = seniorMasterService.create(requestDto,admin.getAdminId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/createMaster")
    public ResponseEntity<ResponseDto> createMaster(@RequestBody CreateEntityRequestDto requestDto, HttpServletRequest request){
        Admin admin = checkAuthorize(request);
     ResponseDto response = masterService.create(requestDto,admin.getAdminId());
     return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @PostMapping("/createAgent")
    public ResponseEntity<ResponseDto> createAgent(@RequestBody CreateEntityRequestDto requestDto,HttpServletRequest request){
      Admin admin = checkAuthorize(request);
      ResponseDto response = agentService.create(requestDto,admin.getAdminId());
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @PostMapping("/createUser")
    public ResponseEntity<ResponseDto> createUser(@RequestBody CreateEntityRequestDto requestDto,HttpServletRequest request){
        Admin admin = checkAuthorize(request);
        ResponseDto response = userService.create(requestDto,admin.getAdminId());
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @GetMapping("/listSeniorMaster")
    public ResponseEntity<List<SeniorMasterResponse>> listSeniorMaster(HttpServletRequest request){
        Admin admin=checkAuthorize(request);
       var seniorMasterList = seniorMasterService.findByAdminId(admin);
       var result =seniorMasterService.mapToSeniorMasterResponse(seniorMasterList);
     return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/listMaster")
    public ResponseEntity<List<MasterResponse>> listMaster(HttpServletRequest request){
        Admin admin = checkAuthorize(request);
        var masterList = masterService.findByAdmin(admin);
        var result = masterService.mapToMasterResponse(masterList);
        return new ResponseEntity<>(result,HttpStatus.OK);

    }

    @GetMapping("/listAgent")
    public ResponseEntity<List<AgentResponse>> listAgent(HttpServletRequest request){
        Admin admin = checkAuthorize(request);
        var agentList = agentService.findByAdmin(admin);
        var result = agentService.mapToAgentResponse(agentList);
    return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/listUser")
    public ResponseEntity<List<UserResponse>> listUser(HttpServletRequest request){
        Admin admin = checkAuthorize(request);
    var userList = userService.findByAdmin(admin);
    var result = userService.mapToUserResponse(userList);
    return new ResponseEntity<>(result,HttpStatus.OK);
    }

    private Admin checkAuthorize(HttpServletRequest request){
        String token = jwtService.getJWTFromRequest(request);
        Admin admin = jwtService.getAdminFromToken(token);
        return admin;
    }


}
