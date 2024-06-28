package com.cyberz.ar7demon.controller;

import com.cyberz.ar7demon.dto.auth.ResponseDto;
import com.cyberz.ar7demon.dto.requestDto.AgentResponse;
import com.cyberz.ar7demon.dto.requestDto.CreateEntityRequestDto;
import com.cyberz.ar7demon.dto.requestDto.UserResponse;
import com.cyberz.ar7demon.model.entity.Master;
import com.cyberz.ar7demon.security.JWTService;
import com.cyberz.ar7demon.service.AgentService;
import com.cyberz.ar7demon.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/master")
public class MasterController {
    @Autowired
    private AgentService agentService;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;

    @PostMapping("/createAgent")
    public ResponseEntity<ResponseDto> createAgent(@RequestBody CreateEntityRequestDto requestDto, HttpServletRequest request){
        Master master = checkAuthority(request);
        var result =agentService.create(requestDto, master.getMasterId());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @PostMapping("/createUser")
    public ResponseEntity<ResponseDto> createUser(@RequestBody CreateEntityRequestDto requestDto,HttpServletRequest request){
        Master master = checkAuthority(request);
       var result = userService.create(requestDto,master.getMasterId());
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @GetMapping("/listAgent")
    public ResponseEntity<List<AgentResponse>> agentList(HttpServletRequest request){
        Master master = checkAuthority(request);
        var agentList =agentService.findByMaster(master);
        var result = agentService.mapToAgentResponse(agentList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @GetMapping("/listUser")
    public ResponseEntity<List<UserResponse>> userList(HttpServletRequest request){
        Master master = checkAuthority(request);
        var userList = userService.findByMaster(master);
        var result = userService.mapToUserResponse(userList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    private Master checkAuthority(HttpServletRequest request){
        String token = jwtService.getJWTFromRequest(request);
        return jwtService.getMasterFromToken(token);
    }
}
