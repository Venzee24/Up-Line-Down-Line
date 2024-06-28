package com.cyberz.ar7demon.dto.requestDto;

import com.cyberz.ar7demon.model.entity.Agent;
import lombok.Data;

import java.util.List;

@Data
public class AgentResponse {
    private Integer agentId;
    private Long unit;
    private String name;
    private String phone;
}
