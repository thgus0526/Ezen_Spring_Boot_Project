package com.ezen.weather.userTemp;

import com.ezen.weather.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserTempRepository extends JpaRepository<UserTemp, Long> {
}
