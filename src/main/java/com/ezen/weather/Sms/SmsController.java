package com.ezen.weather.Sms;

import com.ezen.weather.adminTemp.AdminTempRepository;
import com.ezen.weather.user.SiteUser;
import com.ezen.weather.user.UserRepository;
import com.ezen.weather.user.UserService;
import com.ezen.weather.userTemp.UserTemp;
import com.ezen.weather.userTemp.UserTempRepository;
import com.ezen.weather.userTemp.UserTempService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class SmsController {

    @Value("${message-api-key}")
    private String messageApiKey;

    @Value("${message-secret-api-key}")
    private String messageSecretApiKey;

    private final AdminTempRepository adminTempRepository;
    private final UserRepository userRepository;
    private final UserTempRepository userTempRepository;
    private final UserTempService userTempService;
    private final UserService userService;

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        messageService = NurigoApp.INSTANCE.initialize(messageApiKey, messageSecretApiKey, "https://api.coolsms.co.kr");
    }

    @PostMapping("/user/send")
    public void sendSms(@RequestBody Map<String, Object> data) {
        System.out.println("문자~~~~~~~~~~~~~~~~~");
        System.out.println("온도값 : " + data.get("value").toString());
        System.out.println("아이디값 : " + data.get("userId").toString().trim());
        double currentTemp = Double.parseDouble(data.get("value").toString());
        String userId = data.get("userId").toString().trim();

        Optional<SiteUser> userOptional = userRepository.findByUserId(userId);
        System.out.println("User found by findByUserId: " + userOptional.isPresent());

        if(userOptional.isPresent()) {
            SiteUser user = userOptional.get();
            UserTemp userTemp = userTempService.getUserTemp(userId);

            System.out.println(data.get("userId").toString().trim() + "의 온도 설정 값 : " + userTemp);
            if(userTemp != null) {
                double userMaxTemp = userTemp.getUserSetMaxTemp();

                if(currentTemp > userMaxTemp && user.getSms() >= 1) {
                    String userPhone = user.getPhone();
                    Message message = new Message();
                    message.setFrom("01052850970");
                    message.setTo(userPhone);
                    message.setText("[지금날씨어때] 오늘 00시 00분 00지역에 경계경보 발령. 여러분께서는 대피할 준비를 하시고, 어린이와 노약자가 우선 대피할 수 있도록 해 주시기 바랍니다.");
//                    SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
//                    System.out.println("Message sent: " + response);
                    userService.discountSmsCount(userId);
                }
            }
        }
    }
}
