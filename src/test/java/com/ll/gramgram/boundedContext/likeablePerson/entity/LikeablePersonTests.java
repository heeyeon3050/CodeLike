package com.ll.gramgram.boundedContext.likeablePerson.entity;

import com.ll.gramgram.TestUt;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LikeablePersonTests {
    @Autowired
    private MemberService memberService;
    @Autowired
    private LikeablePersonService likeablePersonService;

    @Test
    @DisplayName("쿨타임 시간이 초단위로 남았을 경우 올림하여 1분으로 출력한다.")
    void t009() {
        Member memberUser3 = memberService.findByUsername("user3").orElseThrow();

        //user3이 insta_user2에게 3이라는 이유로 호감표시
        LikeablePerson likeablePersonToUser2 = likeablePersonService.like(memberUser3, "insta_user2", 3).getData();

        //modifyUnlockDate를 현재시간보다 30초 많게 설정
        TestUt.setFieldValue(likeablePersonToUser2, "modifyUnlockDate", LocalDateTime.now().minusSeconds(-30));

        //호감 표시 변경/호감 취소하기까지 30초 남았다는 뜻이므로 30초는 1분으로 표시됨
        //실제로는 실행시간때문에 29초로 남았다고 인지하고 1분으로 표시됨
        assertEquals("1분", likeablePersonToUser2.getModifyUnlockDateRemainStrHuman());
    }
}