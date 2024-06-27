package com.ezen.weather.Sms;

import com.ezen.weather.adminTemp.AdminTempRepository;

import com.ezen.weather.user.SiteUser;
import com.ezen.weather.user.UserRepository;
import com.ezen.weather.user.UserService;
import com.ezen.weather.userTemp.UserTemp;
import com.ezen.weather.userTemp.UserTempRepository;
import com.ezen.weather.userTemp.UserTempService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.apache.maven.model.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class SmsController {

    private final DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCSZ8XWZKKRP9KKN", "JMPSLYI9E3BNNLB3Y7OJQ9ZWFXZFRXBD", "https://api.coolsms.co.kr");

    private final AdminTempRepository adminTempRepository;
    private final UserRepository userRepository;
    private final UserTempRepository userTempRepository;
    private final UserTempService userTempService;


    @Scheduled(cron = "0 0 6 * * *")    // 오전 6시에 전송되도록 설정
    @PostMapping("/user/send")
    public void sendSms(@RequestBody Map<String, Object> data) {
        System.out.println("문자~~~~~~~~~~~~~~~~~");
        // 현재 기온
        double currentTemp = Double.parseDouble(data.get("value").toString());

        // 모든 사용자 정보 가져오기
        List<SiteUser> allUsers = userRepository.findAll();

        for(SiteUser user : allUsers) {
            String userId = user.getUserId();
            // 사용자별 설정값 가져오기
            UserTemp userTemp = userTempService.getUserTemp(userId);

            if(userTemp != null){
                double userMaxTemp = userTemp.getUserSetMaxTemp();
                double userMinTemp = userTemp.getUserSetMinTemp();
                Integer userRain = userTemp.getUserSetRain(); // 추후 추가

                // 조건을 만족한다면 문자 보내기
                if(currentTemp > userMaxTemp){
                    String userPhone = user.getPhone();

                    Message message = new Message();
                    message.setFrom("01020751709"); // 발신자번호 고정 (유성헌)
                    message.setTo(userPhone);
                    message.setText("현재기온이 설정값을 벗어났습니다 폭염에 주의하세요~!");
                    // 메세지 발송
//                    SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
//                    System.out.println("Message sent: " + response);
                }
            }

        }
    }


}
