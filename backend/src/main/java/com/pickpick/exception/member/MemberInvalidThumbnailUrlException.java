package com.pickpick.exception.member;

import com.pickpick.exception.BadRequestException;

public class MemberInvalidThumbnailUrlException extends BadRequestException {

    private static final String ERROR_CODE = "MEMBER_INVALID_THUMBNAIL_URL";
    private static final String SERVER_MESSAGE = "유효하지 않은 이미지 주소";
    private static final String CLIENT_MESSAGE = "유효하지 않은 이미지 주소입니다.";

    public MemberInvalidThumbnailUrlException(final String thumbnailUrl) {
        super(String.format("%s -> member thumbnail url: %s", SERVER_MESSAGE, thumbnailUrl), CLIENT_MESSAGE,
                ERROR_CODE);
    }
}
