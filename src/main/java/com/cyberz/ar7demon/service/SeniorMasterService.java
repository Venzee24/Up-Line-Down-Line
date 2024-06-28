package com.cyberz.ar7demon.service;

import com.cyberz.ar7demon.dto.requestDto.CreateEntityRequestDto;
import com.cyberz.ar7demon.dto.auth.ResponseDto;
import com.cyberz.ar7demon.dto.requestDto.MasterResponse;
import com.cyberz.ar7demon.dto.requestDto.SeniorMasterResponse;
import com.cyberz.ar7demon.exception.CustomException;
import com.cyberz.ar7demon.model.entity.Admin;
import com.cyberz.ar7demon.model.entity.Role;
import com.cyberz.ar7demon.model.entity.SeniorMaster;
import com.cyberz.ar7demon.repository.AdminRepository;
import com.cyberz.ar7demon.repository.SeniorMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class SeniorMasterService {
    @Autowired
    private SeniorMasterRepository seniorMasterRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CommonService commonService;

    public ResponseDto create(CreateEntityRequestDto requestDto, Integer upLineId){
        if (seniorMasterRepository.existsByEmail(requestDto.getEmail())){
            ResponseDto responseDto = new ResponseDto();
            responseDto.setMessage("Email is already existed.");
            responseDto.setStatus(HttpStatus.BAD_REQUEST.value());
            responseDto.setTimeStamp(new Date());
            return responseDto;
        }
        SeniorMaster seniorMaster = new SeniorMaster();
        Admin admin = adminRepository.findById(upLineId).orElseThrow(()->new CustomException("Admin not found by Id."));
        seniorMaster.setAdmin(admin);
        seniorMaster.setName(requestDto.getName());
        seniorMaster.setEmail(requestDto.getEmail());
        seniorMaster.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        seniorMaster.setPhone(requestDto.getPhone());
        seniorMaster.setRole(Role.SENIOR_MASTER);

        seniorMasterRepository.save(seniorMaster);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setTimeStamp(new Date());
        responseDto.setMessage("SeniorMaster Created Success.");
        responseDto.setStatus(HttpStatus.CREATED.value());
        return responseDto;
    }
    public ResponseDto unitUpdate(Integer unitAmount,Integer id,Character operate){
       SeniorMaster seniorMaster= seniorMasterRepository.findById(id).get();
       if (operate.equals('+')) {
           seniorMaster.setUnit(seniorMaster.getUnit()+unitAmount);
       } else if (operate.equals('-')) {
           seniorMaster.setUnit(seniorMaster.getUnit()-unitAmount);
       }else {
           throw new CustomException("Operate is invalid.");
       }

       ResponseDto responseDto = new ResponseDto();
       responseDto.setStatus(HttpStatus.OK.value());
       responseDto.setMessage("Unit Updated Success.");
       responseDto.setTimeStamp(new Date());
       return responseDto;
    }

    public List<SeniorMaster> findByAdminId(Admin admin) {
       return seniorMasterRepository.findByAdmin(admin).orElseThrow(()->
               new CustomException("adminId not found!"));
    }
    public List<SeniorMasterResponse> mapToSeniorMasterResponse(List<SeniorMaster> seniorMasterList){
        List<SeniorMasterResponse> seniorMasterResponseList = new LinkedList<>();

        seniorMasterList.forEach(a->{
            SeniorMasterResponse seniorMasterResponse = new SeniorMasterResponse();
            seniorMasterResponse.setSeniorMasterId(a.getSeniorMasterId());
            seniorMasterResponse.setPhone(a.getPhone());
            seniorMasterResponse.setName(a.getName());
            seniorMasterResponse.setUnit(a.getUnit());
            seniorMasterResponseList.add(seniorMasterResponse);

        });
        return seniorMasterResponseList;
    }


}
