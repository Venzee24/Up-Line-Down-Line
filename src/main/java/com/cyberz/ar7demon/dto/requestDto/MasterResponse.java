package com.cyberz.ar7demon.dto.requestDto;

import com.cyberz.ar7demon.model.entity.Master;
import lombok.Data;

import java.util.List;

@Data
public class MasterResponse {
    private Integer masterId;
    private Long unit;
    private String name;
    private String phone;
}
