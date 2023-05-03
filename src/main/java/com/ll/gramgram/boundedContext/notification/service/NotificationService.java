package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public RsData make(InstaMember fromInstaMember, InstaMember toInstaMember, String newGender, int newAttractiveTypeCode ){
        Notification notification = Notification
                .builder()
                .fromInstaMember(fromInstaMember)
                .toInstaMember(toInstaMember)
                .typeCode("like")
                .newGender(newGender)
                .newAttractiveTypeCode(newAttractiveTypeCode)
                .build();

        notificationRepository.save(notification);

        return RsData.of("S-1", "호감 표시에 대해 알림을 보냈습니다.");
    }

    @Transactional
    public RsData make(InstaMember fromInstaMember, InstaMember toInstaMember, int oldAttractiveTypeCode, int newAttractiveTypeCode ){
        Notification notification = Notification
                .builder()
                .fromInstaMember(fromInstaMember)
                .toInstaMember(toInstaMember)
                .typeCode("ModifyAttractiveType")
                .oldAttractiveTypeCode(oldAttractiveTypeCode)
                .newAttractiveTypeCode(newAttractiveTypeCode)
                .build();

        notificationRepository.save(notification);

        return RsData.of("S-1", "호감 변경에 대해 알림을 보냈습니다.");
    }
}