package com.cyberz.ar7demon.service;

import com.cyberz.ar7demon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommonService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SeniorMasterRepository seniorMasterRepository;
    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private UserRepository userRepository;

    public Optional<?> checkUpLine(Integer upLineId){
      var admin =  adminRepository.findById(upLineId);
      if (admin.isPresent()){
          return admin;
      }
      var seniorMaster = seniorMasterRepository.findById(upLineId);
      if (seniorMaster.isPresent()){
          return seniorMaster;
      }
      var master = masterRepository.findById(upLineId);
      if (master.isPresent()){
          return master;
      }
      var agent = agentRepository.findById(upLineId);
      if (agent.isPresent()){
          return agent;
      }

      return null;
    }
}
