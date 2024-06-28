package com.cyberz.ar7demon.dto.requestDto;

import com.cyberz.ar7demon.model.entity.SeniorMaster;
import lombok.Data;

import java.util.List;

@Data
public class SeniorMasterResponse {

    private Integer seniorMasterId;
    private Long unit;
    private String name;
    private String phone;
}
