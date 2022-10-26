package com.pickpick.support;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickpick.auth.application.AuthService;
import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.channel.application.ChannelService;
import com.pickpick.channel.application.ChannelSubscriptionService;
import com.pickpick.message.application.BookmarkService;
import com.pickpick.message.application.MessageService;
import com.pickpick.message.application.ReminderService;
import com.pickpick.slackevent.application.SlackEventServiceFinder;
import com.pickpick.workspace.application.WorkspaceService;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
public class DocsControllerTestBase {

    private static final String BEARER_JWT_TOKEN = "Bearer provided.jwt.token";

    @MockBean
    protected AuthService authService;

    @MockBean
    protected ChannelService channelService;

    @MockBean
    protected ChannelSubscriptionService channelSubscriptionService;

    @MockBean
    protected BookmarkService bookmarkService;

    @MockBean
    protected MessageService messageService;

    @MockBean
    protected ReminderService reminderService;

    @MockBean
    protected WorkspaceService workspaceService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected SlackEventServiceFinder slackEventServiceFinder;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected ObjectMapper objectMapper;


    @BeforeEach
    public void setUp(
            final WebApplicationContext context,
            final RestDocumentationContextProvider provider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .build();

        given(jwtTokenProvider.getPayload(any()))
                .willReturn("1");
    }

    protected MockHttpServletRequestBuilder get(final String uri) {
        return MockMvcRequestBuilders
                .get(uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_JWT_TOKEN);
    }

    protected MockHttpServletRequestBuilder getWithParams(final String uri,
                                                          final MultiValueMap<String, String> requestParam) {
        return MockMvcRequestBuilders
                .get(uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_JWT_TOKEN)
                .params(requestParam);
    }

    protected MockHttpServletRequestBuilder post(final String uri, final String body) {
        return MockMvcRequestBuilders
                .post(uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_JWT_TOKEN)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
    }

    protected MockHttpServletRequestBuilder put(final String uri, final String body) {
        return MockMvcRequestBuilders
                .put(uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_JWT_TOKEN)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
    }

    protected MockHttpServletRequestBuilder delete(final String uri,
                                                   final MultiValueMap<String, String> requestParams) {
        return MockMvcRequestBuilders
                .delete(uri)
                .header(HttpHeaders.AUTHORIZATION, BEARER_JWT_TOKEN)
                .params(requestParams);
    }
}
