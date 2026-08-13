package com.fullstack.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INTERNAL_ERROR(5000, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST(4000, "Invalid request", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND(4040, "Resource not found", HttpStatus.NOT_FOUND),

    // Auth
    UNAUTHORIZED(4010, "Unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(4011, "Invalid or expired token", HttpStatus.UNAUTHORIZED),
    INVALID_OTP(4012, "Invalid or expired OTP", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(4090, "User already exists", HttpStatus.CONFLICT),
    BAD_CREDENTIALS(4013, "Wrong username or password", HttpStatus.UNAUTHORIZED),
    USER_NOT_ACTIVE(4014, "User is not active, please verify OTP first", HttpStatus.UNAUTHORIZED),
    USER_BLACKLISTED(4030, "User is blacklisted, please contact support", HttpStatus.FORBIDDEN),
    OTP_ATTEMPTS_EXCEEDED(4031, "OTP attempts exceeded, account is blacklisted", HttpStatus.FORBIDDEN),

    // User
    USER_NOT_FOUND(4041, "User not found", HttpStatus.NOT_FOUND),
    INSUFFICIENT_BALANCE(4001, "Insufficient balance", HttpStatus.BAD_REQUEST),

    // Product / Inventory
    PRODUCT_NOT_FOUND(4042, "Product not found", HttpStatus.NOT_FOUND),
    OUT_OF_STOCK(4002, "Product is out of stock", HttpStatus.BAD_REQUEST),
    PRODUCT_SKU_EXISTS(4091, "Product SKU already exists", HttpStatus.CONFLICT),
    INVALID_IMPORT_FILE(4006, "Import file is invalid", HttpStatus.BAD_REQUEST),
    INVENTORY_NOT_FOUND(4044, "Inventory not found", HttpStatus.NOT_FOUND),

    // Flash sale
    FLASH_SALE_NOT_FOUND(4043, "Flash sale not found", HttpStatus.NOT_FOUND),
    FLASH_SALE_NOT_ACTIVE(4003, "Flash sale is not active", HttpStatus.BAD_REQUEST),
    FLASH_SALE_SOLD_OUT(4004, "Flash sale item is sold out", HttpStatus.BAD_REQUEST),
    PURCHASE_LIMIT_EXCEEDED(4005, "Purchase limit exceeded", HttpStatus.BAD_REQUEST),
    TOO_MANY_REQUESTS(4290, "Too many requests, please try again", HttpStatus.TOO_MANY_REQUESTS);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
