package com.ezen.weather.admin;

import com.ezen.weather.adminTemp.AdminTemp;
import com.ezen.weather.notice.Notice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @Column(name = "admin_id", unique = true, nullable = false)
    private String adminId;

    @Column(name = "admin_pw")
    private String adminPw;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "admin_type", nullable = false)
    private int adminType = 1;

//    @OneToMany(mappedBy = "admin", cascade=CascadeType.ALL)
//    private List<Notice> noticeList = new ArrayList<Notice>();

//    @OneToOne(mappedBy = "admin")
//    private AdminTemp adminTemp;
}
