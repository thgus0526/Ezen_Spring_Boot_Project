package com.ezen.weather.userTemp;

import com.ezen.weather.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_temp")
public class UserTemp {
/////
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_set_max_temp")
    private Double userSetMaxTemp;

    @Column(name = "user_set_min_temp")
    private Double userSetMinTemp;

    @Column(name = "user_set_rain")
    private Integer userSetRain;

    @Column(name = "user_set_fine_dust")
    private Double userSetFineDust;


    @OneToOne
    @JoinColumn(name = "user_id")
    private SiteUser siteUser;


}
