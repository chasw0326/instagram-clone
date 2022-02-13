package com.example.instagram2.usingTDD.passwordTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("비밀번호 강도 테스트")
public class PasswordStrengthMeterTest {

    private static PasswordStrengthMeter meter;

    private void assertStrength(String password, PasswordStrength expStr){
        PasswordStrength result = meter.meter(password);
        assertEquals(expStr, result);
    }

    @BeforeAll
    private static void setUp(){ // BeforeAll의 경우 static으로 선언해야 한다.
        meter = new PasswordStrengthMeter();
    }

    @DisplayName("모든 규칙을 충족하는 경우")
    @Test
    public void meetsAllCriteria_Then_Strong(){
        assertStrength("qw@!3$asDEA", PasswordStrength.STRONG);
        assertStrength("QDseSabc39", PasswordStrength.STRONG);
    }

    @DisplayName("길이만 8글자 미만이고 나머지 조건은 충족하는 경우")
    @Test
    public void meetsOtherCriteria_except_for_Length_Then_Normal(){
        assertStrength("AB12!@", PasswordStrength.NORMAL);
        assertStrength("Qw12qw!", PasswordStrength.NORMAL);
    }

    @DisplayName("숫자를 포함하지 않고 나머지 조건은 충족하는 경우")
    @Test
    public void meetsOtherCriteria_except_for_number_Then_Normal(){
        assertStrength("ABcCbc!@%", PasswordStrength.NORMAL);
        assertStrength("@asKl#dQI!", PasswordStrength.NORMAL);
    }

    @DisplayName("값이 없는 경우1 -null")
    @Test
    public void nullInput_Then_Invalid(){
        assertStrength(null, PasswordStrength.INVALID);
    }

    @DisplayName("값이 없는 경우2 -빈 문자열")
    @Test
    public void emptyInput_Then_Invalid(){
        assertStrength("", PasswordStrength.INVALID);
        assertStrength("     ", PasswordStrength.INVALID);
    }

    @DisplayName("대문자를 포함하지 않고 나머지 조건을 충족하는 경우")
    @Test
    public void meetsOtherCriteria_except_for_Uppercase_Then_Normal(){
        assertStrength("q1b2!@hf&yz", PasswordStrength.NORMAL);
    }

    @DisplayName("길이가 8글자 이상인 조건만 충족하는 경우")
    @Test
    public void meetsOnlyLengthCriteria_Then_Weak(){
        assertStrength("qwertyasdf", PasswordStrength.WEAK);
        assertStrength("examplepw", PasswordStrength.WEAK);
    }

    @DisplayName("숫자 포함 조건만 충족하는 경우")
    @Test
    public void meetsOnlyNumCriteria_Then_Weak(){
        assertStrength("12345", PasswordStrength.WEAK);
        assertStrength("9876543", PasswordStrength.WEAK);
    }

    @DisplayName("대문자 포함 조건만 충족하는 경우")
    @Test
    public void meetsOnlyUpperCriteria_Then_Weak(){
        assertStrength("QWERTY", PasswordStrength.WEAK);
        assertStrength("CHART", PasswordStrength.WEAK);
    }

    @DisplayName("아무 조건도 충족하지 않는 경우")
    @Test
    public void meetsNoCriteria_Then_Weak(){
        assertStrength("cha", PasswordStrength.WEAK);
        assertStrength("orange", PasswordStrength.WEAK);
    }
}
