package com.cyberz.ar7demon.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "agents")
public class Agent implements Serializable {
    private static final long serialVersionUID = 1l;
    @Id
    @GeneratedValue(generator = "agents_generator")
    @SequenceGenerator(name = "agents_generator",allocationSize = 1,initialValue = 1001)
    private Integer agentId;

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
    @JoinColumn(name = "master_id")
    private Master master;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name = "Agent_and_User",joinColumns = @JoinColumn(name = "agent_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> userList;

    public Agent(){
        this.role=Role.AGENT;
    }
}
