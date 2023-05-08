package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.TestUt;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class NotificationServiceTests {
    @Autowired
    private MemberService memberService;
    @Autowired
    private LikeablePersonService likeablePersonService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("새로운 호감 표시 생성 시, 알림 생성")
    void t001() {
        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        Member memberUser2 = memberService.findByUsername("user2").orElseThrow();

        // 호감 표시 하기 이전의 "insta_user2"의 알림 리스트
        List<Notification> notificationList1 = notificationRepository.findByToInstaMember(memberUser2.getInstaMember());

        // "user3"이 "insta_user2"에게 1이라는 이유로 호감 표시
        likeablePersonService.like(memberUser3, "insta_user2", 1);

        // 호감 표시 이후의 "insta_user2"의 알림 리스트
        List<Notification> notificationList2 = notificationRepository.findByToInstaMember(memberUser2.getInstaMember());

        // 호감 표시 이후의 리스트의 크기는 호감 표시 이전의 리스트의 크기보다 1만큼 커야 한다.
        assertThat(notificationList2.size()).isEqualTo(notificationList1.size() + 1);
    }

    @Test
    @DisplayName("기존의 호감 사유 변경 시. 알림 생성")
    void t002() throws Exception {
        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();
        Member memberUser2 = memberService.findByUsername("user2").orElseThrow();

        // "user3"이 "insta_user2"에게 3이라는 이유로 호감 표시
        LikeablePerson likeablePersonToUser2 = likeablePersonService.like(memberUser3, "insta_user2", 3).getData();

        // 호감 표시한 이후의 "insta_user2"의 알림 리스트
        List<Notification> notificationList1 = notificationRepository.findByToInstaMember(memberUser2.getInstaMember());

        // 호감사유 변경하기 위해, modifyUnlockDate를 현재 시간보다 작도록 강제로 설정
        TestUt.setFieldValue(likeablePersonToUser2, "modifyUnlockDate", LocalDateTime.now().minusSeconds(1));

        // "user3"이 likeablePersonToUser2에 대해 호감 사유를 1로 변경
        likeablePersonService.modifyAttractive(memberUser3, likeablePersonToUser2, 1);

        // 호감 사유 변경한 이후의 "insta_user2"의 알림 리스트
        List<Notification> notificationList2 = notificationRepository.findByToInstaMember(memberUser2.getInstaMember());

        // 호감 사유 변경한 이후의 리스트의 크기는 변경하기 이전의 리스트의 크기보다 1만큼 커야 한다.
        assertThat(notificationList2.size()).isEqualTo(notificationList1.size() + 1);
    }
}
