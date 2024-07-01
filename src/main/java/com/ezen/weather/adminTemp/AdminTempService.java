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

    public void input(String hiddenMaxTemp, String hiddenMinTemp, Boolean rain, String id){
        Double hiddenMaxTempDouble = Double.parseDouble(hiddenMaxTemp);
        Double hiddenMinTempDouble = Double.parseDouble(hiddenMinTemp);
        Integer adminTempId = Integer.parseInt(id);

        AdminTemp adminTemp = adminTempRepository.findAdminTempById(adminTempId);

        adminTemp.setAdminSetMaxTemp(hiddenMaxTempDouble);
        adminTemp.setAdminSetMinTemp(hiddenMinTempDouble);
        adminTemp.setAdminSetRain(rain ? 1:0);
        adminTempRepository.save(adminTemp);





    }
}
