package com.ezen.weather.adminTemp;

import com.ezen.weather.admin.Admin;
import com.ezen.weather.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="admin_temp")
public class AdminTemp {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="admin_set_max_temp")
    private Double AdminSetMaxTemp;

    @Column(name="admin_set_min_temp")
    private Double AdminSetMinTemp;

    @Column(name="admin_set_rain")
    private Integer AdminSetRain;

    @Column(name="admin_set_fine_dust")
    private Double AdminSetFineDust;

}
