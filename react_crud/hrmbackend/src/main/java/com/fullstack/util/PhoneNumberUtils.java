package com.fullstack.util;

import com.fullstack.exception.BusinessException;
import com.fullstack.exception.ErrorCode;

import java.util.regex.Pattern;

public final class PhoneNumberUtils {

    /** Phone: bat dau bang 0 va du 10 chu so */
    public static final String PHONE_REGEX = "^0[0-9]{9}$";

    public static final String PHONE_MESSAGE =
            "So dien thoai phai bat dau bang so 0 va gom dung 10 chu so (vi du: 0912345678)";

    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    private PhoneNumberUtils() {
    }

    public static String normalize(String phoneNumber) {
        String trimmed = phoneNumber == null ? "" : phoneNumber.trim();
        if (!PHONE_PATTERN.matcher(trimmed).matches()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, PHONE_MESSAGE);
        }
        return trimmed;
    }
}
