package com.pickpick.message.ui.dto;

import java.util.List;

public interface MessageTextResponses<T extends MessageTextResponse> {

    List<T> findContents();
}
