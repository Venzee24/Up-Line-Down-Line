package com.cyberz.ar7demon.service;

import com.cyberz.ar7demon.dto.auth.ResponseDto;
import com.cyberz.ar7demon.dto.requestDto.AgentResponse;
import com.cyberz.ar7demon.dto.requestDto.CreateEntityRequestDto;
import com.cyberz.ar7demon.exception.CustomException;
import com.cyberz.ar7demon.model.entity.*;
import com.cyberz.ar7demon.repository.AdminRepository;
import com.cyberz.ar7demon.repository.AgentRepository;
import com.cyberz.ar7demon.repository.MasterRepository;
import com.cyberz.ar7demon.repository.SeniorMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class AgentService {
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private SeniorMasterRepository seniorMasterRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseDto create(CreateEntityRequestDto requestDto, Integer upLineId) {
        if (agentRepository.existsByEmail(requestDto.getEmail())){
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("Email is already existed.");
            responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
            responseDto.setTimeStamp(new Date());
            return responseDto;
        }
        Agent agent = new Agent();

        var admin =  adminRepository.findById(upLineId);
        admin.ifPresent(agent::setAdmin);
        var seniorMaster = seniorMasterRepository.findById(upLineId);
        seniorMaster.ifPresent(agent::setSeniorMaster);
        var master = masterRepository.findById(upLineId);
        master.ifPresent(agent::setMaster);

        agent.setName(requestDto.getName());
        agent.setEmail(requestDto.getEmail());
        agent.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        agent.setPhone(requestDto.getPhone());
        agent.setRole(Role.AGENT);

        agentRepository.save(agent);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setTimeStamp(new Date());
        responseDto.setMessage("Agent Created Success.");
        responseDto.setStatus(HttpStatus.CREATED.value());
        return responseDto;
    }

    public List<Agent> findByAdmin(Admin admin) {
        var agentList = agentRepository.findByAdmin(admin).orElseThrow(()->
                new CustomException("Can't find by AdminId "+admin.getAdminId()));
        return agentList;
    }
    public List<AgentResponse> mapToAgentResponse(List<Agent> agentList){
        List<AgentResponse> agentResponsesList = new LinkedList<>();

        agentList.forEach(a->{
            AgentResponse agentResponse = new AgentResponse();
            agentResponse.setAgentId(a.getAgentId());
            agentResponse.setPhone(a.getPhone());
            agentResponse.setName(a.getName());
            agentResponse.setUnit(a.getUnit());
            agentResponsesList.add(agentResponse);

        });
        return agentResponsesList;
    }

    public List<Agent> findBySeniorMaster(SeniorMaster seniorMaster) {
       return agentRepository.findBySeniorMaster(seniorMaster).orElseThrow(()->
                new CustomException("Agent Not Found By SeniorMaster"));
    }

    public List<Agent> findByMaster(Master master) {
        return agentRepository.findByMaster(master).orElseThrow(()->
                new CustomException("Agent Not Found By Master!"));
    }
}
