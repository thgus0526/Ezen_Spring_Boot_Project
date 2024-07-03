package com.ezen.weather;

import com.ezen.weather.notice.Notice;
import com.ezen.weather.notice.NoticeRepository;
import com.ezen.weather.user.SiteUser;
import com.ezen.weather.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootTest
class WeatherApplicationTests {

    @Autowired
    private NoticeRepository noticeRepository;
	@Autowired
	private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


	@Test
	void contextLoads() {
	}

	@Test
	public void testUserSignUp() {

		for (int i = 1; i <= 10; i++) {
			SiteUser user = new SiteUser();
			user.setUserId("user" + i);
			user.setPassword(passwordEncoder.encode("password" + i));
			user.setName("User " + i);
			user.setEmail("user" + i + "@example.com");
			user.setBirth("1994-05-26");
			user.setAddressStreet("서울시 서대문구 홍은동");
			user.setAddressDetail("1111");
			user.setAddressZipcode("11111");
			user.setAddressNotes("홍동");
			user.setPoint(0L);
			user.setAccPoint(0L);
			user.setSms(5);
			userRepository.save(user);
		}
	}

	@Test
	public void testNoticeRegistration() {
		// Given
		Notice notice1 = new Notice();
		notice1.setNoticeTitle("홈페이지 사용 안내");
		notice1.setNoticeContent("안녕하세요,\n" +
				"\n" +
				"How Weather를 이용해 주셔서 감사합니다.\n" +
				"\n" +
				"저희는 고객님께서 설정한 온도를 기준으로 현재 날씨와 비교하여, 설정 온도보다 높거나 낮을 때 문자를 발송하는 서비스를 제공하고 있습니다.\n" +
				"\n" +
				"신규 고객님들은 마이페이지 > 알람설정 > 변경을 클릭하여 원하는 온도와 강수 알림 여부를 선택해주세요. 고객님의 문자 발송 횟수에 따라 1시간마다 알림을 보내드리고 있습니다.\n" +
				"\n" +
				"긴급한 기상 상황 발생 시에는 관리자가 직접 문자를 발송하고 있습니다.\n" +
				"\n" +
				"문의사항이 있으시면 언제든지 문의사항 게시판을 이용해 주세요. 신속히 답변드리겠습니다.\n" +
				"\n" +
				"감사합니다.");
		notice1.setCreateDate(LocalDateTime.now());

		SiteUser adminUser = new SiteUser();
		adminUser.setUserId("admin");
		notice1.setAdminId(adminUser);
		noticeRepository.save(notice1);

		Notice notice2 = new Notice();
		notice2.setNoticeTitle("퀴즈 시스템 안내");
		notice2.setNoticeContent("How Weather를 이용해 주셔서 감사합니다.\n" +
				"\n" +
				"저희 How Weather에서는 운영 중인 퀴즈 시스템에 대해 안내드립니다.\n" +
				"\n" +
				"퀴즈를 맞추시면 해당 퀴즈의 난이도에 따라 포인트가 지급됩니다. 이 포인트는 문자 발송 가능 횟수를 조정하는 데 사용하실 수 있습니다.\n" +
				"\n" +
				"또한, 누적 포인트에 따라 다음과 같은 등급 제도가 운영됩니다:\n" +
				"- 1000점 이하: 브론즈 등급 (5% 할인)\n" +
				"- 1000점 ~ 1999점: 실버 등급 (10% 할인)\n" +
				"- 2000점 ~ 2999점: 골드 등급 (15% 할인)\n" +
				"- 3000점 이상: 플레티넘 등급 (20% 할인)\n" +
				"\n" +
				"문의사항이 있으시면 언제든지 문의사항 게시판을 이용해 주세요. 신속히 답변 드리겠습니다.\n" +
				"\n" +
				"감사합니다.");
		notice2.setCreateDate(LocalDateTime.now());
		adminUser.setUserId("admin");
		notice2.setAdminId(adminUser);
		noticeRepository.save(notice2);

		Notice notice3 = new Notice();
		notice3.setNoticeTitle("불법 이용자 제제 안내");
		notice3.setNoticeContent("안녕하세요,\n" +
				"\n" +
				"How Weather에서 안내드립니다.\n" +
				"\n" +
				"존경하는 고객님께서는 저희 서비스를 이용하시는 동안 중요한 역할을 맡아 주셨습니다. 저희는 모든 사용자에게 공평한 환경을 제공하고자 합니다. 이에 따라 불법적인 방법으로 서비스를 이용하시는 경우, 저희는 이를 제재하게 되어 있습니다.\n" +
				"\n" +
				"당사의 이용 약관을 준수하며 안전하고 투명한 서비스 이용을 원칙으로 하고 있습니다. 만약 이용 규정을 위반하거나 불법적인 활동을 감지할 경우, 관련 조치를 취하게 될 수 있음을 알려드립니다.\n" +
				"\n" +
				"더 나은 서비스 환경을 위해 항상 노력하고 있으며, 여러분의 협조와 양해를 부탁드립니다.\n" +
				"\n" +
				"감사합니다.");
		notice3.setCreateDate(LocalDateTime.now());
		notice3.setAdminId(adminUser);
		noticeRepository.save(notice3);

	}


}
