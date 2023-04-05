## Title: [1Week] 김희연

### 미션 요구사항 분석 & 체크리스트

---

**호감 상대 삭제**

[x] 호감 상대 삭제 테스트 코드 작성

[x] 삭제 시, 삭제 권한이 있는지 체크

[x] 삭제 후, 다시 호감 목록 페이지로 이동

<br>

**구글 로그인**

[x] 구글 로그인으로 가입한 회원인 providerTypeCode : GOOGLE

[x] 로그인 후, username 출력

<br>

### 1주차 미션 요약

---

**호감 상대 삭제**
- 삭제버튼을 누르면, 해당 항목은 삭제되어야 한다.
- 삭제 후 다시 호감 목록 페이지로 돌아와야 한다. (rq.redirectWithMsg 함수 사용)

**구글 로그인**
- 구글 로그인으로 가입 및 로그인 처리(스프링 OAuth2 클라이언트로 구현)
- 구글 로그인으로 가입한 회원의 providerTypeCode : GOOGLE

<br>

[접근 방법]
- 삭제 권한 체크

<첫번째 시도>

현재 로그인한 회원의 정보가 있어야한다는 생각에 `Principal`을 사용하였다.

`Principal.getName()`과 비교하기 위해서 `호감을 표시한 멤버의 username`이 필요했다.

[`호감을 표시한 멤버의 username` 구하기]

1. 호감을 받은 받은 사람 객체를 구하기
2. 호감 받은 사람에 대해 호감을 표시한 사람의 인스타 아이디 구하기
3. 인스타 아이디에 해당하는 멤버 객체 구하기

`호감 표시를 한 멤버의 아이디`와 `현재 로그인한 회원의 username`이 같은지 확인

```java
@PreAuthorize("isAuthenticated()")
@GetMapping("/delete/{id}")
public String delete(Principal principal, @PathVariable("id") int id){
    //호감을 받은 사람(LikeablePerson) - 호감을 표시한 사람(InstaMember) - 회원가입한 모든 회원(Member) 가 모두 연결되어 있다.
    LikeablePerson likeablePerson = likeablePersonService.getLikeablePerson(id); //호감을 받은 사람 객체 구하기
    Long InstaMemberId = likeablePerson.getFromInstaMember().getId(); //호감 받은 사람에 대해 호감 표시를 한 사람의 인스타 아이디 구하기
    Member member = memberService.getMember(InstaMemberId).get(); //인스타 아이디에 해당하는 멤버 객체 구하기
    if (!member.getUsername().equals(principal.getName())) { //호감 표시를 멤버의 아이디와 현재 로그인한 회원의 아이디와 같은지 확인하기
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
    }
    likeablePersonService.delete(likeablePerson);
    return rq.redirectWithMsg("/likeablePerson/list", "삭제");
}
```
<br>

<두번째 시도>

이미 만들어놓은 `Rq`를 사용하면 조금 더 편리해질 것 같았다.

```java

@GetMapping("/delete/{id}")
public String delete(@PathVariable("id") int id){
    //호감을 받은 사람(LikeablePerson) - 호감을 표시한 사람(InstaMember) - 회원가입한 모든 회원(Member) 가 모두 연결되어 있다.
    LikeablePerson likeablePerson = likeablePersonService.getLikeablePerson(id); //호감을 받은 사람 객체 구하기
    Long InstaMemberId = likeablePerson.getFromInstaMember().getId(); //호감 받은 사람에 대해 호감 표시를 한 사람의 인스타 아이디 구하기
    Member member = memberService.getMember(InstaMemberId).get(); //인스타 아이디에 해당하는 멤버 객체 구하기
    if (!member.getUsername().equals(rq.getMember().getUsername())) { //호감 표시를 멤버의 아이디와 현재 로그인한 회원의 아이디와 같은지 확인하기
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
    }
    likeablePersonService.delete(likeablePerson);
    return rq.redirectWithMsg("/likeablePerson/list", "삭제");
}
```
<br>

<세번째 시도_최종코드>

강사님의 힌트로, 삭제 권한을 확인하는 조건은 `멤버의 username`이 아닌 `인스타 아이디`라는 것을 알게되었다.

- 호감을 표시한 사람이 삭제 권한이 있으므로 호감을 표시한 사람인지를 확인해야 한다.
- 호감을 표시한 사람은 인스타 아이디로 확인가능하다.

`호감을 표시한 멤버의 인스타 아이디` 와 `현재 로그인한 회원의 인스타 아이디` 가 같은지 확인

```java
@GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        LikeablePerson likeablePerson = likeablePersonService.getLikeablePerson(id); //호감을 받은 사람 객체 구하기
        if (likeablePerson.getFromInstaMember().getId() != rq.getMember().getInstaMember().getId()) { //"호감을 표시한 멤버의 인스타 아이디"와 "현재 로그인한 회원의 인스타 아이디"가 같은지 확인하기
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        likeablePersonService.delete(likeablePerson);
        return rq.redirectWithMsg("/likeablePerson/list", "삭제");
    }
```

첫번째 코드가 틀렸다고는 할 수 없지만, 삭제 조건에 대해 많이 돌아가서 구현했다면, 세번째 코드는 훨씬 간결해진 것을 볼 수 있다!

<br>

- 구글 계정 접속

이전에 구현한 카카오 로그인과 관련된 소스 코드를 참고하였다.

`CustomOAuth2UserService`, `SecurityConfig`, `MemberService-join()`, `MemberService-whenSocialLogin` 등이 관련되어 있었지만, 구글 로그인 또한 함께 써도 무방하였다.

그래서 수정한 코드는 `application.yml` 코드 뿐이다.
Google에 맞춰서 수정하기 위해서 구글 검색을 많이 하였고, 여러 블로그들을 참고하였다.

또한, `client-id`와 `client-secret`을 구하기 위해 계정을 생성하였다.

<br>

**[특이사항]**
- 삭제 버튼을 눌렀으나, 삭제가 되지 않았다.
-> `@Transactional(readOnly = true)` 때문이었다. `@Transactional`을 붙여주어야 한다.


- 구글 소셜 로그인과 관련한 블로그들은 많았지만, 이들을 조합해서 나에게 맞는 소스 코드로 적용하기까지 시간이 많이 소요되었다.