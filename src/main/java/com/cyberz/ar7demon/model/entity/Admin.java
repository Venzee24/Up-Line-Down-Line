package com.cyberz.ar7demon.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "admins")
public class Admin implements Serializable {
    private static final long serialVersionUID = 1l;
    @Id
    @GeneratedValue(generator ="admins_seq" )
    @SequenceGenerator(name = "admins_seq",allocationSize = 1,initialValue = 0000010)
    private Integer adminId;
    @Column(length = 45,nullable = false)
    private String name;
    @Column(length = 50,nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long unit;
    private boolean activated;

    private boolean locked;

    private LocalDateTime validPassDate;

    private LocalDateTime retiredDate;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name = "Admin_and_SeniorMaster",joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "seniorMaster_id")
    )
    private List<SeniorMaster> seniorMasterList;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name = "Admin_and_Master",joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "master_id")
    )
    private List<Master> masterList;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name = "Admin_and_Agent",joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "agent_id")
    )
    private List<Agent> agentList;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name = "Admin_and_User",joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> userList;



    public Admin(){
        this.role=Role.ADMIN;
    }

}
