package com.pickpick.exception;

public class MemberInvalidThumbnailUrlException extends BadRequestException {

    private static final String DEFAULT_MESSAGE = "유효하지 않은 이미지 주소입니다.";

    public MemberInvalidThumbnailUrlException(final String thumbnailUrl) {
        super(String.format("%s -> member thumbnail url: %s", DEFAULT_MESSAGE, thumbnailUrl));
    }
}
