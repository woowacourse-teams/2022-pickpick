package com.pickpick.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickpick.auth.application.AuthService;
import com.pickpick.auth.support.JwtTokenProvider;
import com.pickpick.auth.ui.AuthController;
import com.pickpick.channel.application.ChannelService;
import com.pickpick.channel.application.ChannelSubscriptionService;
import com.pickpick.channel.ui.ChannelController;
import com.pickpick.channel.ui.ChannelSubscriptionController;
import com.pickpick.message.application.BookmarkService;
import com.pickpick.message.application.MessageService;
import com.pickpick.message.application.ReminderService;
import com.pickpick.message.ui.BookmarkController;
import com.pickpick.message.ui.MessageController;
import com.pickpick.message.ui.ReminderController;
import com.pickpick.slackevent.application.SlackEventServiceFinder;
import com.pickpick.slackevent.ui.SlackEventController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
@WebMvcTest({
        AuthController.class,
        ChannelController.class,
        ChannelSubscriptionController.class,
        BookmarkController.class,
        MessageController.class,
        ReminderController.class,
        SlackEventController.class
})
@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfiguration.class)
public class RestDocsTestSupport {

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
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected SlackEventServiceFinder slackEventServiceFinder;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected ObjectMapper objectMapper;

//    @Autowired
//    private DatabaseCleaner databaseCleaner;

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
    }

    @BeforeEach
    void setup() {
        given(jwtTokenProvider.getPayload(any()))
                .willReturn("1"); // memberId를 반환
    }

//    @AfterEach
//    void clear() {
//        databaseCleaner.clear();
//    }
}
