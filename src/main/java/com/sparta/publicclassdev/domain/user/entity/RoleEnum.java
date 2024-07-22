package com.sparta.publicclassdev.domain.user.entity;

public enum RoleEnum {
    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN),  // 관리자 권한
    WITHDRAW(Authority.WITHDRAW); // 휴면 계정
    private final String authority;

    RoleEnum(String authority) {
        this.authority = authority;
    }
    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String WITHDRAW = "ROLE_WITHDRAW";
    }
}
