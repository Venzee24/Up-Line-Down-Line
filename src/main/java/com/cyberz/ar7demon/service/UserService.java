package com.cyberz.ar7demon.service;

import com.cyberz.ar7demon.dto.auth.ResponseDto;
import com.cyberz.ar7demon.dto.requestDto.CreateEntityRequestDto;
import com.cyberz.ar7demon.dto.responseDto.UserResponse;
import com.cyberz.ar7demon.exception.CustomException;
import com.cyberz.ar7demon.model.entity.*;
import com.cyberz.ar7demon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserService {
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
    private PasswordEncoder passwordEncoder;


    public ResponseDto create(CreateEntityRequestDto requestDto, Integer upLineId) {
        if (userRepository.existsByEmail(requestDto.getEmail())){
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("Email is already existed.");
            responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
            responseDto.setTimeStamp(new Date());
            return responseDto;
        }
        User user = new User();
        var admin =  adminRepository.findById(upLineId);
        admin.ifPresent(user::setAdmin);
        var seniorMaster = seniorMasterRepository.findById(upLineId);
        seniorMaster.ifPresent(user::setSeniorMaster);
        var master = masterRepository.findById(upLineId);
        master.ifPresent(user::setMaster);
        var agent = agentRepository.findById(upLineId);
        agent.ifPresent(user::setAgent);
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setPhone(requestDto.getPhone());
        user.setRole(Role.USER);

        userRepository.save(user);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setTimeStamp(new Date());
        responseDto.setMessage("User Created Success.");
        responseDto.setStatus(HttpStatus.CREATED.value());
        return responseDto;
    }

    public List<User> findByAdmin(Admin admin) {
        var userList = userRepository.findByAdmin(admin).orElseThrow(()->
                new CustomException("Can't find by AdminId "+admin.getAdminId()));
        return userList;

    }
    public List<UserResponse> mapToUserResponse(List<User> userList){
        List<UserResponse> userResponseList = new LinkedList<>();

        userList.forEach(a->{
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(a.getUserId());
            userResponse.setPhone(a.getPhone());
            userResponse.setName(a.getName());
            userResponse.setUnit(a.getUnit());
            userResponseList.add(userResponse);

        });
        return userResponseList;
    }


    public List<User>  findByAgent(Agent agent) {
        return userRepository.findByAgent(agent).orElseThrow(()->
                new CustomException("User Not Found By Agent!"));
    }

    public List<User> findBySeniorMaster(SeniorMaster seniorMaster) {
       return userRepository.findBySeniorMaster(seniorMaster).orElseThrow(()->
                new CustomException("User Not Found By SeniorMaster!"));
    }

    public List<User> findByMaster(Master master) {
        return userRepository.findByMaster(master).orElseThrow(()->
                new CustomException("User Not Found By Master!"));
    }

    public List<User> findByUserName(String name){
        return userRepository.findAll(byUserName(name));
    }

    private Specification<User> byUserName(String name){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name".toLowerCase()),name.toLowerCase().concat("%"));
    }
}
