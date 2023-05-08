package com.ll.gramgram.boundedContext.notification.controller;

import com.ll.gramgram.TestUt;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class NotificationControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private LikeablePersonService likeablePersonService;

    @Test
    @DisplayName("새로운 호감 표시 생성 시, 알림 생성")
    @WithUserDetails("user2")
    void t001() throws Exception {
        Member member = memberService.findByUsername("user3").orElseThrow();

        likeablePersonService.like(member, "insta_user2", 1);

        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/notification/list"))
                .andDo(print());
        // THEN
        resultActions
                .andExpect(handler().handlerType(NotificationController.class))
                .andExpect(handler().methodName("showList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        때문에 좋아합니다.
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("기존의 호감 사유 수정 시, 수정 알림 생성")
    @WithUserDetails("user2")
    void t002() throws Exception {
        Member member = memberService.findByUsername("user3").orElseThrow();

        // "user3"이 "insta_user2"에게 호감 표시
        LikeablePerson likeablePerson = likeablePersonService.like(member, "insta_user2", 1).getData();

        // 호감사유 변경하기 위해, modifyUnlockDate를 현재 시간보다 작도록 강제로 설정
        TestUt.setFieldValue(likeablePerson, "modifyUnlockDate", LocalDateTime.now().minusSeconds(1));

        // "user3"이 "insta_user2"에 대한 호감 사유 변경
        likeablePersonService.modifyAttractive(member, likeablePerson, 3);

        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/notification/list"))
                .andDo(print());
        // THEN
        resultActions
                .andExpect(handler().handlerType(NotificationController.class))
                .andExpect(handler().methodName("showList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        </span>으로 변경했습니다.
                        """.stripIndent().trim())));
    }
}