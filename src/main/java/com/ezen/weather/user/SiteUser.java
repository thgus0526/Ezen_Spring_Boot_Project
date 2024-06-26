package com.ezen.weather.user;

import com.ezen.weather.userTemp.UserTemp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name= "user")
public class SiteUser {

    @Id
    @Column(name="user_id", nullable = false, unique = true)
    private String userId;

    @Column(name="user_pw", nullable = false)
    private String password;

    @Column(name="user_name", nullable=false)
    private String name;

    @Column(name="user_phone")
    private String phone;

    @Column(name="user_address_street")
    private String addressStreet;

    @Column(name="user_address_zipcode")
    private String addressZipcode;

    @Column(name="user_address_detail")
    private String addressDetail;

    @Column(name="user_address_notes")
    private String addressNotes;

    @Column(name="user_email", unique=true, nullable=false)
    private String email;

    @Column(name="user_grade")
    private String grade;

    @Column(name="user_birth")
    private String birth;

    @Column(name="user_point")
    private Long point = 100l;

    @Column(name="user_point_acc")
    private Long accPoint = 100l;

    // 문자횟수를 위한 칼럼추가
    @Column(name="user_sms")
    private int sms=5;
    
    @OneToOne(mappedBy = "siteUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserTemp userTemp;

}
