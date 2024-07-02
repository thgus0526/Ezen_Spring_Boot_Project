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

    private final DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("NCSOAMYK1HONAHU1", "G9F2CS3B69XTX81XG8HOUTSRWXQ3M0UL", "https://api.coolsms.co.kr");

    private final AdminTempRepository adminTempRepository;
    private final UserRepository userRepository;
    private final UserTempRepository userTempRepository;
    private final UserTempService userTempService;
    private final UserService userService;



    @PostMapping("/user/send")
    public void sendSms(@RequestBody Map<String, Object> data) {
        System.out.println("문자~~~~~~~~~~~~~~~~~");
        System.out.println("온도값 : " + data.get("value").toString());
        System.out.println("아이디값 : " + data.get("userId").toString().trim());
        // 현재 기온
        double currentTemp = Double.parseDouble(data.get("value").toString());

        String userId = data.get("userId").toString().trim();

        // findByUserId 사용
        Optional<SiteUser> userOptional = userRepository.findByUserId(userId);
        System.out.println("User found by findByUserId: " + userOptional.isPresent());

        if(userOptional.isPresent()) {
            SiteUser user = userOptional.get();
            UserTemp userTemp = userTempService.getUserTemp(userId);

            System.out.println(data.get("userId").toString().trim()+"의 온도 설정 값 : "+userTemp);
            if(userTemp != null){
                double userMaxTemp = userTemp.getUserSetMaxTemp();

                if(currentTemp > userMaxTemp && user.getSms() >= 1){
                    String userPhone = user.getPhone();
                    Message message = new Message();
                    message.setFrom("01022300705");
                    message.setTo(userPhone);
                    message.setText("[지금날씨어때] 오늘 00시 00분 00지역에 경계경보 발령. 여러분께서는 대피할 준비를 하시고, 어린이와 노약자가 우선 대피할 수 있도록 해 주시기 바랍니다.");
                    SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
                    System.out.println("Message sent: " + response);
                    userService.discountSmsCount(userId);
                }
            }
        }


//        if (userTemp != null) {
//            double userMaxTemp = userTemp.getUserSetMaxTemp();
//            double userMinTemp = userTemp.getUserSetMinTemp();
//            Integer userRain = userTemp.getUserSetRain();
//
//            if (currentTemp > userMaxTemp && siteUser.getSms() >= 1) {
//                String userPhone = siteUser.getPhone();
//                System.out.println("로직통과한 유저 아이디 : " + siteUser.getUserId());
//                Message message = new Message();
//                message.setFrom("01022300705"); // 발신자번호 고정 (유성헌)
//                message.setTo(userPhone);
//                message.setText("현재기온이 설정값을 벗어났습니다 폭염에 주의하세요~!");
//                // 메세지 발송
//                // SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
//                // System.out.println("Message sent: " + response);
//                userService.discountSmsCount(userId);
//            }
//        }
//        SiteUser siteUser = userRepository.findById(userId).orElse(null);
        // 사용자별 설정값 가져오기
//        UserTemp userTemp = userTempService.getUserTemp(userId);

//        if(userTemp != null){
//            double userMaxTemp = userTemp.getUserSetMaxTemp();
//            double userMinTemp = userTemp.getUserSetMinTemp();
//            Integer userRain = userTemp.getUserSetRain(); // 추후 추가
//
//
//            // 조건을 만족한다면 문자 보내기
//            if(currentTemp > userMaxTemp && siteUser.getSms() >= 1){
//                String userPhone = siteUser.getPhone();
//                System.out.println("로직통과한 유저 아이디 : " + siteUser.getUserId());
//                Message message = new Message();
//                message.setFrom("01022300705"); // 발신자번호 고정 (유성헌)
//                message.setTo(userPhone);
//                message.setText("현재기온이 설정값을 벗어났습니다 폭염에 주의하세요~!");
//                // 메세지 발송
////                SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
////                System.out.println("Message sent: " + response);
//                userService.discountSmsCount(userId);
//            }
//        }

    }
}



