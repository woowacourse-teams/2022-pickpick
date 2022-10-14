package com.pickpick.support;

import com.pickpick.auth.application.dto.BotInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.pickpick.slackevent.domain.Participation;
import com.pickpick.workspace.domain.Workspace;
import java.util.List;

public interface ExternalClient {

    String callUserToken(String code);

    BotInfoDto callBotInfo(String code);

    String callMemberSlackId(String accessToken);

    List<Member> findAllWorkspaceMembers(Workspace workspace);

    List<Channel> findChannelsByWorkspace(Workspace workspace);

    Participation findParticipation(String userToken);

    void sendMessage(Reminder reminder);
}
