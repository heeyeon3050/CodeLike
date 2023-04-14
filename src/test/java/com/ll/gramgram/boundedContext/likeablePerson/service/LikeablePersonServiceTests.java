package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private LikeablePersonRepository likeablePersonRepository;

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

    @Test
    @DisplayName("JPA에서 _를 사용하면 관련 엔티티의 속성을 조건으로 검색할 수 있다.")
    void t005() throws Exception {
        // 좋아하는 사람이 2번 인스타 회원인 `좋아요` 검색
        /*
        SELECT l1_0.id,
        l1_0.attractive_type_code,
        l1_0.create_date,
        l1_0.from_insta_member_id,
        l1_0.from_insta_member_username,
        l1_0.modify_date,
        l1_0.to_insta_member_id,
        l1_0.to_insta_member_username
        FROM likeable_person l1_0
        WHERE l1_0.from_insta_member_id = 2
        */
        List<LikeablePerson> likeablePeople = likeablePersonRepository.findByFromInstaMemberId(2L);

        // 좋아하는 대상의 아이디가 insta_user100 인 `좋아요`들 만 검색
        /*
        SELECT l1_0.id,
        l1_0.attractive_type_code,
        l1_0.create_date,
        l1_0.from_insta_member_id,
        l1_0.from_insta_member_username,
        l1_0.modify_date,
        l1_0.to_insta_member_id,
        l1_0.to_insta_member_username
        FROM likeable_person l1_0
        LEFT JOIN insta_member t1_0
        ON t1_0.id=l1_0.to_insta_member_id
        WHERE t1_0.username = "insta_user100";
        */
        List<LikeablePerson> likeablePeople2 = likeablePersonRepository.findByToInstaMember_username("insta_user100");

        assertThat(likeablePeople2.get(0).getId()).isEqualTo(2);

        // 좋아하는 사람이 2번 인스타 회원이고, 좋아하는 대상의 인스타아이디가 "insta_user100" 인 `좋아요`
        /*
        SELECT l1_0.id,
        l1_0.attractive_type_code,
        l1_0.create_date,
        l1_0.from_insta_member_id,
        l1_0.from_insta_member_username,
        l1_0.modify_date,
        l1_0.to_insta_member_id,
        l1_0.to_insta_member_username
        FROM likeable_person l1_0
        LEFT JOIN insta_member t1_0
        ON t1_0.id=l1_0.to_insta_member_id
        WHERE l1_0.from_insta_member_id = 2
        AND t1_0.username = "insta_user100";
        */
        LikeablePerson likeablePerson = likeablePersonRepository.findByFromInstaMemberIdAndToInstaMember_username(2L, "insta_user100");

        assertThat(likeablePerson.getId()).isEqualTo(2);
    }
}

