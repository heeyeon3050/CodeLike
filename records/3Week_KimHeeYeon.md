## Title: [3Week] 김희연

### 미션 요구사항 분석 & 체크리스트

---

### 필수 미션

**호감 표시/호감 사유 변경 후, 3시간 이후에 호감 취소/호감 사유 변경**

[x] getModifyUnlockDateRemainStrHuman() 메소드 구현

[x] 호감 표시 후, 3시간 이후에 호감 취소/호감 사유 변경 가능한 테스트 코드 작성

[x[ 호감 사유 변경 후, 3 시간 이후에 호감 취소/호감 사유 변경 가능한 테스트 코드 작성

### 선택 미션

**알림 기능**

- 호감표시를 받았거나, 본인에 대한 호감사유가 변경된 경우에 알림페이지에서 확인이 가능
- 각각의 알림은 생성시에 `readDate` 가 `null` 이고, 사용자가 알림을 읽으면 `readDate` 가 `현재날짜` 로 세팅

[x] 새로운 호감 표시 생성 시, 알림 생성되는 테스트 코드 작성

[x] 기존의 호감에 대해 호감 사유 변경 시, 알림 생성되는 테스트 코드 작성

[x] 알림버튼 클릭 시, readDate 설정 (readDate가 null인 경우에만)

[x] 최신 알림에 대해서만 아이콘으로 표시

[x] 최신순으로 알림 정렬

### 3주차 미션 요약

---

### [접근 방법]

### 필수 미션

**호감 표시/호감 사유 변경 후, 3시간 이후에 호감 취소/호감 사유 변경**

- getModifyUnlockDateRemainStrHuman() 메소드 구현
    - 해당 호감 사항에 대한 남은 쿨타임을 구현
    - Duration을 이용하여 modifyUnlockDate와 현재 시간의 차이를 구함
    - 초단위에 대해서는 올림
    - 남은 시간이 1시간 이하일 경우에는 ‘분’만 출력
- 쿨타임이 지났는지 확인하는 함수인 `likeablePerson.isModifyUnlocked()` 를 사용

### 선택 미션

**알림 기능**

- 호감 표시 생성 시, `instaMemberServie::whenAfterLike()`에서 `notificationService.make()`를 호출
- `notificationService::make()` 를 통해 알림 생성
    - 새로운 값이 생성되므로 `new Gender`, `newAttractiveTypeCode` 를 받아옴

        ```java
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
        ```


- 호감 사유 변경 시, `instaMemberService::whenAfterModifyAttractiveType()`에서 `notificationService.make()`를 호출
- `notificationService::make()` 를 통해 알림 생성
    - 기존의 호감사유에서 새로운 호감사유로 변경하므로 `oldAttractivTypeCode`, `newAttractiveTypeCode` 를 받아옴

        ```java
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
        ```


- 알림 버튼 클릭 시, readDate 설정 (readDate가 null인 경우에만)
    - 현재 로그인한 회원의 인스타아이디가 `toInstaMember`에 있는 경우들을 알림리스트에 담음
    - 알림들을에 대해서 `null`일 경우에만 @setter 를 이용하여 값 설정
- 생성 날짜에 대한 포맷 형식을 “yy-MM-dd HH:mm” 으로 바꾸기 위해 `DateTimeFormatter` 사용
    - 처음에는 thymleaf 문법을 사용하여 진행하였는데, 멘토님의 피드백을 통해 entity에서 값을 가져오는 형식으로 구현
- 현재 날짜로부터 알림이 생성된 시간이 몇 분전인지, 몇 시간 전이지 확인하기 위해 `getDiffrenceTimeStrHuman()` 구현
- `<i class="fa-solid fa-fire text-red-500"></i>`아이콘 출력 조건을 최신 글일 때만 출력
    - `th:if="${notification.readDate != null and notification.readDate eq currentTime}"`
- ‘W’, ‘M’이 아닌 ‘여성’, ‘남성’을 출력하기 위해 `notification.fromInstaMember.getGenderDisplayName()`
- `AttractiveTypeCode`가 1이 아닌 ‘외모’로 출력되기 위해 `getAttractiveTypeDisplayName()` 구현
- 최신 알림이 맨 위에 출력될 수 있도록 notifications를 구할 때, `findByToInstaMemberOrderByCreateDateDesc()` 를 사용

### **[특이 사항]**

- `NotificationService`의 `updateReadDate()`에서 이미 `readDate` 값이 있는 알림들까지 모두 가져와서 readDate를 설정해주는데, 이 경우는 알림이 많아질 수 록 비효율적이라는 생각이 들어서 더 좋은 방법이 있는지 궁금하다.