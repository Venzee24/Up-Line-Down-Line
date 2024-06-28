package com.cyberz.ar7demon.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1l;
    @Id
    @GeneratedValue(generator = "users_generator")
    @SequenceGenerator(name = "users_generator", allocationSize = 1,initialValue = 000100)
    private Integer userId;
    @Column(length = 45, nullable = false)
    private String name;
    @Column( nullable = false)
    private String password;
    @Column(length = 12, nullable = false)
    private String phone;
    private Long unit;
    @Column(length = 50,nullable = false,unique = true)
    private String email;


    private boolean activated;

    private boolean locked;

    private LocalDateTime validPassDate;

    private LocalDateTime retiredDate;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "seniorMaster_id")
    private SeniorMaster seniorMaster;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    public User(){
        this.role=Role.USER;
    }
}
