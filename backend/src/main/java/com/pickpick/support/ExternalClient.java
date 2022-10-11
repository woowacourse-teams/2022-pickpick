package com.pickpick.support;

import com.pickpick.auth.application.dto.BotInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.pickpick.workspace.domain.Workspace;
import java.util.List;
import java.util.Map;

public interface ExternalClient {

    String callUserToken(String code);

    BotInfoDto callBotInfo(String code);

    String callMemberSlackId(String accessToken);

    Channel callChannel(String channelSlackId, Workspace workspace);

    List<Member> findAllWorkspaceMembers(Workspace workspace);

    List<Channel> findChannelsByWorkspace(Workspace workspace);

    Map<String, Boolean> findParticipation(String userToken);

    void sendMessage(Reminder reminder);
}
