package com.pickpick.service;


import com.pickpick.entity.Channel;
import com.pickpick.repository.ChannelRepository;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Conversation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChannelInitializer {

    @Value("${slack.bot-token}")
    private String slackBotToken;

    private final MethodsClient client = Slack.getInstance().methods();
    private final ChannelRepository channels;

    public ChannelInitializer(ChannelRepository channels) {
        this.channels = channels;
    }

    @PostConstruct
    void setupMember() throws SlackApiException, IOException {
        List<String> savedSlackIds = findSavedSlackIds();
        List<Channel> currentWorkspaceChannels = getCurrentChannels();
        List<Channel> channelsToSave = filterChannelsToSave(savedSlackIds, currentWorkspaceChannels);

        channels.saveAll(channelsToSave);
    }

    private List<String> findSavedSlackIds() {
        return channels.findAll()
                .stream()
                .map(Channel::getSlackId)
                .collect(Collectors.toList());
    }

    private List<Channel> getCurrentChannels() throws IOException, SlackApiException {
        return toChannels(client.conversationsList(request -> request
                .token(slackBotToken)).getChannels());
    }

    private List<Channel> filterChannelsToSave(final List<String> savedSlackIds, final List<Channel> currentWorkspaceChannels) {
        return currentWorkspaceChannels.stream()
                .filter(it -> !savedSlackIds.contains(it.getSlackId()))
                .collect(Collectors.toList());
    }

    private List<Channel> toChannels(final List<Conversation> conversations) {
        return conversations.stream()
                .map(this::toChannel)
                .collect(Collectors.toList());
    }

    private Channel toChannel(final Conversation conversation) {
        return new Channel(conversation.getId(), conversation.getName());
    }
}
