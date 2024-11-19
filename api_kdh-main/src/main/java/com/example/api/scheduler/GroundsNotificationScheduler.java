package com.example.api.scheduler;

import com.example.api.entity.Grounds;
import com.example.api.repository.GroundsRepository;
import com.example.api.repository.GroundsReviewsRepository;
import com.example.api.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class GroundsNotificationScheduler {

  @Autowired
  private GroundsRepository groundsRepository;

  @Autowired
  private GroundsReviewsRepository groundsReviewsRepository;

  @Autowired
  private MailService mailService;

  @Scheduled(cron = "0 */5 * * * *") // 매시간 정각에 실행
  public void sendGroundsNotification() {
    LocalDateTime now = LocalDateTime.now();

    // 모든 경기 정보를 가져옴
    List<Grounds> groundsList = groundsRepository.findAll();

    for (Grounds ground : groundsList) {
      // 경기 시작 시간을 가져옴
      LocalDateTime groundDateTime = ground.getGroundsDateTime();

      // 경기 시작 1시간 전에 알림 발송
      if (groundDateTime.minusHours(1).isBefore(now) && groundDateTime.isAfter(now)) {
        System.out.println("경기 알림 발송 대상: " + ground.getGtitle());

        // 예약자 이메일 리스트 조회
        List<String> emails = groundsReviewsRepository.findEmailsByGroundId(ground.getGno());

        if (emails.isEmpty()) {
          System.out.println("예약자가 없는 경기: " + ground.getGtitle());
          continue;
        }

        // 각 예약자에게 메일 전송
        for (String email : emails) {
          if (email == null || email.isEmpty()) {
            System.out.println("잘못된 이메일이 포함된 경기: " + ground.getGno());
            continue;
          }

          // 이메일 내용 작성
          String subject = "[알림] 예약하신 경기가 곧 시작됩니다!";
          String text = String.format("안녕하세요, 예약하신 경기 (%s)가 1시간 뒤에 시작됩니다. 준비해주세요!",
              ground.getGtitle());

          // 메일 전송
          mailService.sendMail(email, subject, text);
        }
      }
    }
  }
}
