package com.cyberz.ar7demon.dto.requestDto;

import com.cyberz.ar7demon.model.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;
@Data
public class CreateEntityRequestDto {

    private String name;

    private String password;

    private String phone;

    private String email;




}
