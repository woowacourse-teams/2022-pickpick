package com.pickpick.exception.message;

import com.pickpick.exception.SlackApiCallException;

public class SlackSendMessageFailureException extends SlackApiCallException {

    private static final String DEFAULT_MESSAGE = "슬랙 메시지 전송 API 호출 실패";

    public SlackSendMessageFailureException(final String error) {
        super(String.format("%s -> 에러: %s", DEFAULT_MESSAGE, error));
    }
}
