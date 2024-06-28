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
@Table(name = "masters")
public class Master implements Serializable {
    private static final long serialVersionUID = 1l;
    @Id
    @GeneratedValue(generator = "masters_generator")
    @SequenceGenerator(name = "masters_generator",allocationSize = 1,initialValue = 10001)
    private Integer masterId;
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

    @ManyToOne
    @JoinColumn(name = "seniorMaster_id")
    private SeniorMaster seniorMaster;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name = "Master_and_Agent",joinColumns = @JoinColumn(name = "master_id"),
            inverseJoinColumns = @JoinColumn(name = "agent_id")
    )
    private List<Agent> agentList;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name = "Master_and_User",joinColumns = @JoinColumn(name = "master_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> userList;

    @Enumerated(EnumType.STRING)
    private Role role;
    public Master(){
        this.role=Role.MASTER;
    }
}
