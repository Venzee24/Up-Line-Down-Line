package com.cyberz.ar7demon.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "tokens")
@Getter
@Setter
public class AuthenticationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    @Column(name = "created_date")
    private Date createdDate;

    @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(targetEntity = Admin.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToOne(targetEntity = SeniorMaster.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "senior_master_id")
    private SeniorMaster seniorMaster;

    @OneToOne(targetEntity = Master.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "master_id")
    private Master master;

    @OneToOne(targetEntity = Agent.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "agent_id")
    private Agent agent;






}
