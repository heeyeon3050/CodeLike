package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<Notification> findByToInstaMember(InstaMember toInstaMember) {
        return notificationRepository.findByToInstaMember(toInstaMember);
    }

    @Transactional
    public RsData<Notification> makeLike(LikeablePerson likeablePerson) {
        return make(likeablePerson, "LIKE", 0, null);
    }

    @Transactional
    public RsData<Notification> makeModifyAttractive(LikeablePerson likeablePerson, int oldAttractiveTypeCode) {
        return make(likeablePerson, "MODIFY_ATTRACTIVE_TYPE", oldAttractiveTypeCode, likeablePerson.getFromInstaMember().getGender());
    }

    private RsData<Notification> make(LikeablePerson likeablePerson, String typeCode, int oldAttractiveTypeCode, String oldGender) {
        Notification notification = Notification
                .builder()
                .typeCode(typeCode)
                .toInstaMember(likeablePerson.getToInstaMember())
                .fromInstaMember(likeablePerson.getFromInstaMember())
                .oldAttractiveTypeCode(oldAttractiveTypeCode)
                .oldGender(oldGender)
                .newAttractiveTypeCode(likeablePerson.getAttractiveTypeCode())
                .newGender(likeablePerson.getFromInstaMember().getGender())
                .build();

        notificationRepository.save(notification);

        return RsData.of("S-1", "알림 메세지가 생성되었습니다.", notification);
    }

    @Transactional
    public RsData<List<Notification>> updateReadDate(InstaMember instaMember, LocalDateTime currentTime) {
        List<Notification> notifications = findByToInstaMember(instaMember); //나를 좋아하는 것에 대한 알림들
        for(Notification notification : notifications){
            if(notification.getReadDate() == null) { //최초로 읽은 날짜를 기록하기 위해, readDate가 null인 경우에만 현재 날짜를 설정
                notification.setReadDate(currentTime);
                System.out.println(notification.getReadDate());
                notificationRepository.save(notification);
            }
        }

        return RsData.of("S-1", "readDate가 설정되었습니다.", notifications);
    }
}