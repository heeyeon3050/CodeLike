## Title: [4Week] 김희연

### 미션 요구사항 분석 & 체크리스트

---

### 필수 미션

****네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용****

- `https://도메인/` 형태로 접속이 가능
- 운영서버에서 각종 소셜로그인, 인스타 아이디 연결이 잘 되어야 함

****내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현****

- 내가 받은 호감리스트에서 특정 성별을 가진 사람에게서 받은 호감만 필터링

[x] 남성을 조회하면 남성이 호감표시한 리스트들만 출력

[x] 여성을 조회하면 여성이 호감표시한 리스트들만 출력

### 선택 미션

**젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포 진행**

- 수행하지 못함

**내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현**

- 내가 받은 호감리스트에서 특정 호감사유의 호감만 필터링

[x] 내가 받은 호감리스트에서 호감사유에 해당하는 리스트들만 출력

****내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능****

- 최신순(기본)
    - 최근에 받은 호감표시를 우선적으로 표시
- 날짜순
    - 오래전에 받은 호감표시를 우선적으로 표시
- 인기 많은 순
    - 인기가 많은 사람의 호감표시를 우선적으로 표시
- 인기 적은 순
    - 인기가 적은 사람의 호감표시를 우선적으로 표시
- 성별순
    - 여성에게 받은 호감표시를 먼저 표시하고, 그 다음 남자에게 받은 호감표시를 후에 표시
    - 2순위 정렬조건으로는 최신순
- 호감사유순
    - 외모 때문에 받은 호감표시를 먼저 표시하고, 그 다음 성격 때문에 받은 호감표시를 후에 표시, 마지막으로 능력 때문에 받은 호감표시를 후에 표시
    - 2순위 정렬조건으로는 최신순

### 4주차 미션 요약

---

### [접근 방법]

### 필수 미션

****네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용****

- 강사님 가이드 영상 보면서 진행

****내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현****

- `LikeablePersonController` 에서 `@RequestParam` 을 통해 `gender` 값을 받아옴
- `if (gender != null && gender.length() > 0)`
    - 처음 `/usr/likeablePerson/toList` 로 접속했을 때 `gender` 자체가 없을 경우에 `null` 이 올 수 도 있고, `gender=` 일 경우에는  `""` 빈문자열이 올 수 도 있다.
    - 두 경우를 모두 판단하여 조건문을 구현하였다.
- 조건은 맞은 거 같은데 출력이 되지 않아 어려움을 겪었었다.
    - Stream 형식으로 변환한 값을 바로 출력하도록 했는데, List로 형식으로 바꿔서 출력을 했어야 했다.

  [처음 코드]

    ```java
    if(gender != null && gender.length() > 0){
                    stream = stream.filter(e -> e.getFromInstaMember().getGender().equals(gender));
    					   }
    
    model.addAttribute("likeablePeople", stream);
    ```

  [개선 코드]

    ```java
    if(gender != null && gender.length() > 0){
                    stream = stream.filter(e -> e.getFromInstaMember().getGender().equals(gender));
                }
    
    List<LikeablePerson> newData = stream.collect(Collectors.toList()); //List로 변환
    
    model.addAttribute("likeablePeople", newData);
    ```


### 선택 미션

**내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현**

- `@RequestParam(defaultValue = "0") int attractiveTypeCode`
    - `/usr/likeablePerson/toList` 로 접속했을 때 `attractiveattractiveTypeCode` 자체가 없을 경우에 `“0”` 으로 되고 int형으로 형변환되어  `0` 이 된다.
    - `attractiveTypeCode=` 일 경우와 같이 값이 없을 경우에는 빈문자열 `""`이 들어가는데, int 형으로 형변환되어 초기값 `0` 이 된다.
    - 따라서 `0`에 대한 처리만 해주면 된다.
- `if (attractiveTypeCode != 0)`
    - 호감사유의 값이 지정됐을 경우에만 해당 호감사유에 맞게 출력

****내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능****

- switch 문 사용
- 최신순(기본)
    - case 1
    - `Comparator.*comparing*(LikeablePerson::getModifyDate, Comparator.*reverseOrder*())`
    - 수정된 날짜를 기준으로 내림차순으로 정렬
- 날짜순
    - case 2
    - `Comparator.*comparing*(LikeablePerson::getModifyDate)`
    - 수정된 날짜를 기준으로 오름차순으로 정렬
- 인기 많은 순
    - case 3
    - `Comparator.*comparingInt*(e -> -e.getFromInstaMember().getToLikeablePeople().size())`
    - `호감 표시를 한 사람` 의 좋아요들의 개수를 비교하여 내림차순으로 정렬
    - 내림차순으로 정렬하기 위해 `-` 를 붙였다.
- 인기 적은 순
    - case 4
    - `Comparator.*comparingInt*(e -> e.getFromInstaMember().getToLikeablePeople().size())`
    - `호감 표시를 한 사람` 의 좋아요들의 개수를 비교하여 오름차순으로 정렬
- 성별순
    - case 5
    - `(LikeablePerson e) -> e.getFromInstaMember().getGender().equals("W") ? 0 : 1)`
    - 호감 표시한 사람의 성별이 여성이라면 0, 아니라면 1을 두어 여성이 앞에 오게끔 하였다.
    - `.thenComparing(LikeablePerson::getModifyDate, Comparator.*reverseOrder*()`
    - 성별이 같을 경우 최신순으로 정렬하기 위해 `.thenComparing` 을 사용하였다.
- 호감사유순
    - case 6
    - `Comparator.*comparing*((LikeablePerson e) -> e.getAttractiveTypeCode())        .thenComparing(LikeablePerson::getModifyDate, Comparator.*reverseOrder*()));`
    - `외모, 성격, 능력`의 `attractiveTypeCode`가 `1, 2, 3` 이므로 오름차순으로 정렬

### **[특이 사항]**

- 정렬하는 부분에서 case 3, case 4 는 `e -> -e.getFromInstaMember()` 와 같은 형식으로 `FromInstaMember` 를 불러올 수 있었는데,
  case 5, case 6 은 `e -> -e.getFromInstaMember()` 와 같은 형식으로 구현하면 **`getFromInstaMember()에 대해서 Cannot resolve method 'getFromInstaMember' in 'Object'`**라는 오류가 떴다. → 메소드를 찾을 수 없다.

  그래서 **명시적으로 LikeablePerson 유형을 지정**해야만 했는데, 그 이유가 궁금하다.