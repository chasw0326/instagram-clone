package com.example.instagram2.serviceTests;


import com.example.instagram2.dto.SignUpDTO;
import com.example.instagram2.repository.MemberRepository;
import com.example.instagram2.security.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthSvcTests {

    @Autowired
    private AuthUtil authService;

    @Autowired
    private MemberRepository repository;
//
//    @DisplayName("휴대폰 번호로 가입하고 나머지 모든게 정상 데이터")
//    @Test
//    public void signupWithPhoneNumTest() {
//        String phoneOrEmail = "01032322225";
//        String name = "핫식스";
//        String username = "hotSix6";
//        String password = "WTEsdl123!@";
//        SignUpDTO dto = SignUpDTO.builder()
//                .phoneOrEmail(phoneOrEmail)
//                .name(name)
//                .username(username)
//                .password(password)
//                .build();
//
//
//        List<String> errorState = authService.signUp(dto);
//        List<String> expState = new ArrayList<>();
//        System.out.println(errorState);
//        Member expMember = repository.getByPhoneNum(phoneOrEmail);
//        assertEquals(expMember.getPhoneNum(), phoneOrEmail);
//        assertNull(expMember.getEmail());
//        assertEquals(expMember.getName(), name);
//        assertEquals(expMember.getUsername(), username);
//        expState.add("Good");
//        assertEquals(expState, errorState);
//        repository.delete(expMember);
//    }
//
//    // 이거 안되네 내일 해야함
//    @DisplayName("이메일 주소로 가입하고 나머지 모든게 정상 데이터")
//    @Test
//    public void signupWithEmailTest() {
//        String phoneOrEmail = "bcaaeaa@naver.com";
//        String name = "핫식스";
//        String username = "hotSix6";
//        String password = "WTEsdl123!@";
//
//        SignUpDTO dto = SignUpDTO.builder()
//                .phoneOrEmail(phoneOrEmail)
//                .name(name)
//                .username(username)
//                .password("WTEsdl123!@")
//                .build();
//
//        List<String> errorState = authService.signUp(dto);
//        List<String> expState = new ArrayList<>();
//        System.out.println(errorState);
//        Member expMember = repository.getByEmail(phoneOrEmail);
//        assertEquals(expMember.getEmail(), phoneOrEmail);
//        assertNull(expMember.getPhoneNum());
//        assertEquals(expMember.getName(), name);
//        assertEquals(expMember.getUsername(), username);
//        expState.add("Good");
//        assertEquals(expState, errorState);
//        repository.delete(expMember);
//
//    }
//
//    @DisplayName("아이디를 공백으로 가입했을때")
//    @Test
//    public void signupWithEmptyPhoneNumOrEmail() {
//        String phoneOrEmail = "";
//        String name = "핫식스";
//        String username = "hotSix6";
//        String password = "WTEsdl123!@";
//        SignUpDTO dto = SignUpDTO.builder()
//                .phoneOrEmail(phoneOrEmail)
//                .name(name)
//                .username(username)
//                .password(password)
//                .build();
//
//        List<String> errorState = authService.signUp(dto);
//        List<String> expState = new ArrayList<>();
//        expState.add("Invalid phoneOrEmail");
//        expState.add("Wrong email pattern");
//        System.out.println(errorState);
//        assertEquals(expState, errorState);
//        assertNull(repository.getByUsername(username));
//    }
//
//    @DisplayName("약한 비밀번호로 가입했을때")
//    @Test
//    public void signupWithWeakPassword() {
//        String phoneOrEmail = "bcaaeaa@naver.com";
//        String name = "핫식스";
//        String username = "hotSix6";
//        String password = "123456";
//        SignUpDTO dto = SignUpDTO.builder()
//                .phoneOrEmail(phoneOrEmail)
//                .name(name)
//                .username(username)
//                .password(password)
//                .build();
//
//        List<String> errorState = authService.signUp(dto);
//        List<String> expState = new ArrayList<>();
//
//        expState.add("Too weak password");
//        System.out.println(errorState);
//        assertEquals(expState, errorState);
//        assertNull(repository.getByUsername(username));
//    }
//
//    @DisplayName("중복된 이메일로 가입했을때")
//    @Test
//    public void signupWithDuplicateEmail() {
//        String phoneOrEmail = "test200@example.com";
//        String name = "핫식스";
//        String username = "hotSix6";
//        String password = "WTEsdl123!@";
//        SignUpDTO dto = SignUpDTO.builder()
//                .phoneOrEmail(phoneOrEmail)
//                .name(name)
//                .username(username)
//                .password(password)
//                .build();
//
//        List<String> errorState = authService.signUp(dto);
//        List<String> expState = new ArrayList<>();
//        System.out.println(errorState);
//        expState.add("Email already exists");
//        assertEquals(expState, errorState);
//    }
//
//    @DisplayName("중복된 username으로 가입했을때")
//    @Test
//    public void signupWithDuplicateUsername(){
//        String phoneOrEmail = "test2022@example.com";
//        String name = "핫식스";
//        String username = "200번이름";
//        String password = "WTEsdl123!@";
//        SignUpDTO dto = SignUpDTO.builder()
//                .phoneOrEmail(phoneOrEmail)
//                .name(name)
//                .username(username)
//                .password(password)
//                .build();
//
//        List<String> errorState = authService.signUp(dto);
//        List<String> expState = new ArrayList<>();
//        System.out.println(errorState);
//        expState.add("Duplicated username");
//        assertEquals(expState, errorState);
//    }
//
//    @DisplayName("이름 안 적었을때")
//    @Test
//    public void signupWithEmptyName(){
//        String phoneOrEmail = "test2022@example.com";
//        String name = "";
//        String username = "123123번이름";
//        String password = "WTEsdl123!@";
//        SignUpDTO dto = SignUpDTO.builder()
//                .phoneOrEmail(phoneOrEmail)
//                .name(name)
//                .username(username)
//                .password(password)
//                .build();
//
//        List<String> errorState = authService.signUp(dto);
//        List<String> expState = new ArrayList<>();
//        System.out.println(errorState);
//        expState.add("Invalid name");
//        assertEquals(expState, errorState);
//    }
//
//    @DisplayName("폰번호나 이메일 안 적었을때")
//    @Test
//    public void signupWithEmptyPhoneOrEmail(){
//        String phoneOrEmail = "";
//        String name = "핫식스";
//        String username = "20023번이름";
//        String password = "WTEsdl123!@";
//        SignUpDTO dto = SignUpDTO.builder()
//                .phoneOrEmail(phoneOrEmail)
//                .name(name)
//                .username(username)
//                .password(password)
//                .build();
//
//        List<String> errorState = authService.signUp(dto);
//        List<String> expState = new ArrayList<>();
//        System.out.println(errorState);
//        expState.add("Invalid phoneOrEmail");
//        expState.add("Wrong email pattern");
//        assertEquals(expState, errorState);
//    }
//
//    @DisplayName("유저네임 안 적었을때")
//    @Test
//    public void signupWithEmptyUserName(){
//        String phoneOrEmail = "test2022@example.com";
//        String name = "핫식스";
//        String username = "";
//        String password = "WTEsdl123!@";
//        SignUpDTO dto = SignUpDTO.builder()
//                .phoneOrEmail(phoneOrEmail)
//                .name(name)
//                .username(username)
//                .password(password)
//                .build();
//
//        List<String> errorState = authService.signUp(dto);
//        List<String> expState = new ArrayList<>();
//        System.out.println(errorState);
//        expState.add("Invalid username");
//        assertEquals(expState, errorState);
//    }
//
//
//
//    @Test
//    public void isEmailPatternTest() {
//        String pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
//        System.out.println(Pattern.matches(pattern, "bcaaeaa@naver.com"));
//    }
//
//    @Test
//    public void isPhoneNumTest() {
//        String pattern = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
//        assertFalse(Pattern.matches(pattern, "test200@naver.com"));
//        assertTrue(Pattern.matches(pattern, "01012345678"));
//    }
//
//    @Test
//    public void dummyTest() {
//        Member member = repository.getByUsername("hotSix6");
//        repository.delete(member);
//    }
//
//    @DisplayName("비밀번호 변경 테스트")
//    @Test
//    public void ChangePwTest(){
//        Member member = repository.getByEmail("bcaaeaa@naver.com");
//        Long mno = member.getMno();
//        PasswordDTO dto = PasswordDTO.builder()
//                .mno(mno)
//                .oldPw("ABCDefg12!@")
//                .newPw("abcDe12#@")
//                .checkNewPw("abcDe12#@")
//                .build();
//
//        assertEquals("비밀번호 수정완료", authService.changePassword(dto));
//    }
//
//    @DisplayName("체크 비밀번호가 틀렸을 경우")
//    @Test
//    public void ChangePwTest2(){
//        Member member = repository.getByEmail("bcaaeaa@naver.com");
//        Long mno = member.getMno();
//        PasswordDTO dto = PasswordDTO.builder()
//                .mno(mno)
//                .oldPw("abcDe12#@")
//                .newPw("abcABC12!@")
//                .checkNewPw("abcABC12!")
//                .build();
//        assertEquals("체크 비밀번호가 다릅니다.", authService.changePassword(dto));
//    }
//
//    @DisplayName("입력한 비밀번호가 현재 비밀번호랑 다른 경우")
//    @Test
//    public void ChangePwTest3(){
//        Member member = repository.getByEmail("bcaaeaa@naver.com");
//        Long mno = member.getMno();
//        PasswordDTO dto = PasswordDTO.builder()
//                .mno(mno)
//                .oldPw("abcDe12#")
//                .newPw("abcABC12!@")
//                .checkNewPw("abcABC12!@")
//                .build();
//        assertEquals("현재 비밀번호랑 다릅니다.", authService.changePassword(dto));
//    }
//
//    @DisplayName("비밀번호 강도가 약할 경우")
//    @Test
//    public void ChangePwTest4(){
//        Member member = repository.getByEmail("bcaaeaa@naver.com");
//        Long mno = member.getMno();
//        PasswordDTO dto = PasswordDTO.builder()
//                .mno(mno)
//                .oldPw("abcABC12")
//                .newPw("abcd")
//                .checkNewPw("abcd")
//                .build();
//        assertEquals("비밀번호 강도가 약합니다.", authService.changePassword(dto));
//    }
//
//
    @Test
    public void dummy(){
        String email = "asd@naver.com";
        String name = "핫식스";
        String username = "who";
        String password = "abcABC123";
        SignUpDTO dto = SignUpDTO.builder()
                .email(email)
                .name(name)
                .username(username)
                .password(password)
                .build();

        authService.signUp(dto);
//        System.out.println(errorState);
    }
}
