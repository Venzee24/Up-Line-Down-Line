package com.cyberz.ar7demon.dto.requestDto;

import lombok.Data;

@Data
public class UserResponse {
    private Integer userId;
    private Long unit;
    private String name;
    private String phone;
}
