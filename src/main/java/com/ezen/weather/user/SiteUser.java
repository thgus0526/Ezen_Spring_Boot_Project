package com.ezen.weather.user;

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
    private Long point;

    @Column(name="user_type", nullable=false)
    private int userType=0;


}
