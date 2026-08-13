package com.fullstack.entity;

public enum UserStatus {
    UNVERIFIED,
    ACTIVE,
    LOCKED,
    /** Bi vo hieu hoa do nhap sai OTP qua so lan cho phep, khong the dang ky/login lai */
    BLACK_LIST
}
