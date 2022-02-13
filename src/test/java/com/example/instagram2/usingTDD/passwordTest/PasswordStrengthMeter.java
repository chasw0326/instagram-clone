package com.example.instagram2.usingTDD.passwordTest;

public class PasswordStrengthMeter {

    public PasswordStrength meter(String s) {
        if (s == null || s.trim().isEmpty()) return PasswordStrength.INVALID;
        int metCnt = getMetCriteriaCounts(s);

        if (metCnt <= 1) return PasswordStrength.WEAK;
        if (metCnt == 2) return PasswordStrength.NORMAL;

        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean meetsContainingUppercaseCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }

    private int getMetCriteriaCounts(String s) {
        int metCnt = 0;
        if (s.length() >= 8) metCnt++;
        if (meetsContainingNumberCriteria(s)) metCnt++;
        if (meetsContainingUppercaseCriteria(s)) metCnt++;
        return metCnt;
    }
}
