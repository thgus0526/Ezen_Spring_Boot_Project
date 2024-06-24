package com.ezen.weather.adminTemp;

import com.ezen.weather.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminTempRepository extends JpaRepository<AdminTemp, Integer> {
}

