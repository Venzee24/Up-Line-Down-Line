package com.cyberz.ar7demon.service;

import com.cyberz.ar7demon.dto.auth.ResponseDto;
import com.cyberz.ar7demon.dto.requestDto.CreateEntityRequestDto;
import com.cyberz.ar7demon.dto.responseDto.MasterResponse;
import com.cyberz.ar7demon.exception.CustomException;
import com.cyberz.ar7demon.model.entity.Admin;
import com.cyberz.ar7demon.model.entity.Master;
import com.cyberz.ar7demon.model.entity.Role;
import com.cyberz.ar7demon.model.entity.SeniorMaster;
import com.cyberz.ar7demon.repository.AdminRepository;
import com.cyberz.ar7demon.repository.MasterRepository;
import com.cyberz.ar7demon.repository.SeniorMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class MasterService {
    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SeniorMasterRepository seniorMasterRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseDto create(CreateEntityRequestDto requestDto, Integer upLineId) {
        if (masterRepository.existsByEmail(requestDto.getEmail())){
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("Email is already existed.");
            responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
            responseDto.setTimeStamp(new Date());
            return responseDto;
        }
        Master master = new Master();
        var admin =  adminRepository.findById(upLineId);
        admin.ifPresent(master::setAdmin);
        var seniorMaster = seniorMasterRepository.findById(upLineId);
        seniorMaster.ifPresent(master::setSeniorMaster);

        master.setName(requestDto.getName());
        master.setEmail(requestDto.getEmail());
        master.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        master.setPhone(requestDto.getPhone());
        master.setRole(Role.MASTER);

        masterRepository.save(master);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setTimeStamp(new Date());
        responseDto.setMessage("Master Created Success.");
        responseDto.setStatus(HttpStatus.CREATED.value());
        return responseDto;
    }

    public List<Master> findByAdmin(Admin admin) {
        var masterList = masterRepository.findByAdmin(admin).orElseThrow(()->
                new CustomException("Can't find by adminId "+admin.getAdminId()));
        return masterList;

    }
    public List<MasterResponse> mapToMasterResponse(List<Master> masterList){
        List<MasterResponse> masterResponseList = new LinkedList<>();

        masterList.forEach(a->{
            MasterResponse userResponse = new MasterResponse();
            userResponse.setMasterId(a.getMasterId());
            userResponse.setPhone(a.getPhone());
            userResponse.setName(a.getName());
            userResponse.setUnit(a.getUnit());
            masterResponseList.add(userResponse);

        });
        return masterResponseList;
    }

    public List<Master> findBySeniorMaster(SeniorMaster seniorMaster) {
       return masterRepository.findBySeniorMaster(seniorMaster).orElseThrow(()->new CustomException("Master Not Found By SeniorMaster"));
    }
    public List<Master> findByMasterName(String name){
        return masterRepository.findAll(byMasterName(name));
    }
    private Specification<Master> byMasterName(String name){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name".toLowerCase()),name.toLowerCase().concat("%"));
    }
}
