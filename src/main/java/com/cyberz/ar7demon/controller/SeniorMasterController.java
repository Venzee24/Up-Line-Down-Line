package com.cyberz.ar7demon.controller;

import com.cyberz.ar7demon.dto.auth.ResponseDto;
import com.cyberz.ar7demon.dto.requestDto.AgentResponse;
import com.cyberz.ar7demon.dto.requestDto.CreateEntityRequestDto;
import com.cyberz.ar7demon.dto.requestDto.MasterResponse;
import com.cyberz.ar7demon.dto.requestDto.UserResponse;
import com.cyberz.ar7demon.model.entity.SeniorMaster;
import com.cyberz.ar7demon.security.JWTService;
import com.cyberz.ar7demon.service.AgentService;
import com.cyberz.ar7demon.service.MasterService;
import com.cyberz.ar7demon.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seniorMaster")
public class SeniorMasterController {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private MasterService masterService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private UserService userService;

    @PostMapping("/createMaster")
    public ResponseEntity<ResponseDto> createMaster(@RequestBody CreateEntityRequestDto requestDto, HttpServletRequest request){
       SeniorMaster seniorMaster = checkAuthority(request);
       var result =masterService.create(requestDto,seniorMaster.getSeniorMasterId());
       return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @PostMapping("/createAgent")
    public ResponseEntity<ResponseDto> createAgent(@RequestBody CreateEntityRequestDto requestDto,HttpServletRequest request){
        SeniorMaster seniorMaster = checkAuthority(request);
        var result = agentService.create(requestDto,seniorMaster.getSeniorMasterId());
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }
    @PostMapping("/createUser")
    public ResponseEntity<ResponseDto> createUser(@RequestBody CreateEntityRequestDto requestDto,HttpServletRequest request){
        SeniorMaster seniorMaster = checkAuthority(request);
        var result = userService.create(requestDto,seniorMaster.getSeniorMasterId());
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @GetMapping("/listMaster")
    public ResponseEntity<List<MasterResponse>> masterList(HttpServletRequest request){
        SeniorMaster seniorMaster = checkAuthority(request);
        var masterList =masterService.findBySeniorMaster(seniorMaster);
       var result = masterService.mapToMasterResponse(masterList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @GetMapping("/listAgent")
    public ResponseEntity<List<AgentResponse>> agentList(HttpServletRequest request){
        SeniorMaster seniorMaster = checkAuthority(request);
        var agentList = agentService.findBySeniorMaster(seniorMaster);
        var result = agentService.mapToAgentResponse(agentList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/listUser")
    public ResponseEntity<List<UserResponse>> userList(HttpServletRequest request){
        SeniorMaster seniorMaster = checkAuthority(request);
        var userList = userService.findBySeniorMaster(seniorMaster);
        var result = userService.mapToUserResponse(userList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    private SeniorMaster checkAuthority(HttpServletRequest request){
        String token = jwtService.getJWTFromRequest(request);
        return jwtService.getSeniorMasterFromToken(token);
    }
}
