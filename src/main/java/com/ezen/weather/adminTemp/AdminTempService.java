package com.ezen.weather.adminTemp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminTempService {

    @Autowired
    private AdminTempRepository adminTempRepository;

    public List<AdminTemp> getAllAdminTemps() {
        return adminTempRepository.findAll();
    }
}
