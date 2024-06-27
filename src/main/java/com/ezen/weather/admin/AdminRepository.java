package com.ezen.weather.admin;

import com.ezen.weather.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, String> {

}
