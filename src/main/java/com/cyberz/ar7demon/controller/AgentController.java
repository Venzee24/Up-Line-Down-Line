package com.cyberz.ar7demon.controller;

import com.cyberz.ar7demon.dto.auth.ResponseDto;
import com.cyberz.ar7demon.dto.requestDto.CreateEntityRequestDto;
import com.cyberz.ar7demon.dto.requestDto.UserResponse;
import com.cyberz.ar7demon.model.entity.Agent;
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
@RequestMapping("/agent")
public class AgentController {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AgentService agentService;

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<ResponseDto> createUser(@RequestBody CreateEntityRequestDto requestDto, HttpServletRequest request){

        String token = jwtService.getJWTFromRequest(request);
        Integer agentId = jwtService.getAgentFromToken(token).getAgentId();

       var result = userService.create(requestDto,agentId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }

    @GetMapping("/userList")
    public ResponseEntity<List<UserResponse>> userList(HttpServletRequest request){
       Agent agent = checkAuthorize(request);
      var userList = userService.findByAgent(agent);
     var userResponseList = userService.mapToUserResponse(userList);
      return new ResponseEntity<>(userResponseList,HttpStatus.OK);
    }
    private Agent checkAuthorize(HttpServletRequest request){
        String token = jwtService.getJWTFromRequest(request);
       return jwtService.getAgentFromToken(token);

    }
}
