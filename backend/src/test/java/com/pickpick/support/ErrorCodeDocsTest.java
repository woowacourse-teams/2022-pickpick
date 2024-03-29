package com.pickpick.support;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class ErrorCodeDocsTest extends DocsControllerTestBase {

    @DisplayName("Rest Docs에 에러코드를 생성합니다.")
    @Test
    void errorCodes() throws Exception {
        ErrorCodeSnippet errorCodeSnippet = new ErrorCodeSnippet("error-code", "error-code-template");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/test/error-code"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(errorCodeSnippet));
    }
}
