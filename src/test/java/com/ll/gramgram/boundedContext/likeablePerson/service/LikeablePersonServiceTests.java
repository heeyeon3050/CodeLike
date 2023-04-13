package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class LikeablePersonServiceTests {
    @Autowired
    private LikeablePersonService likeablePersonService;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("user3이 user4에게 중복으로 호감표시 할 수 없다.")
    void t001() {
        Member member = memberService.findByUsername("user3").get();

        RsData<LikeablePerson> likeRsData = likeablePersonService.like(member, "insta_user4", 1);

        assertThat(likeRsData.getResultCode()).isEqualTo("F-3");
        assertThat(likeRsData.getMsg()).isEqualTo("인스타유저(insta_user4)는 이미 등록되어있습니다.");

    }

    @Test
    @DisplayName("한 명의 instaMember가 11명 이상의 호감상대를 등록할 수 없다.")
    void t002() {
        Member member = memberService.findByUsername("user3").get();

        for (int i = 5; i < 13; i ++){
            likeablePersonService.like(member, "insta_user"+i, 1);
        }

        RsData<LikeablePerson> likeRsData = likeablePersonService.like(member, "insta_user13", 1);

        assertThat(likeRsData.getResultCode()).isEqualTo("F-4");
        assertThat(likeRsData.getMsg()).isEqualTo("호감표시는 10명까지 등록가능합니다.");
    }

    @Test
    @DisplayName("인스타 아이디가 같아도 다른 유형의 호감표시는 가능하다.")
    void t003() {
        Member member = memberService.findByUsername("user3").get();

        RsData<LikeablePerson> likeRsData = likeablePersonService.like(member, "insta_user4", 2);

        assertThat(likeRsData.getResultCode()).isEqualTo("S-2");
        assertThat(likeRsData.getMsg()).isEqualTo( "인스타유저(insta_user4)에 대한 호감사유가 외모에서 성격(으)로 변경되었습니다.");
    }

    @Test
    @DisplayName("설정파일에 있는 최대가능호감표시 수 가져오기")
    void t004() throws Exception {
        long likeablePersonFromMax = AppConfig.getLikeablePersonFromMax();

        assertThat(likeablePersonFromMax).isEqualTo(10);
    }
}

