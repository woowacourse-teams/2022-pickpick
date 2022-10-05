package com.pickpick.support;

import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.pickpick.workspace.domain.Workspace;
import java.util.List;

public interface ExternalClient {

    String callAccessToken(String code);

    String callMemberSlackId(String accessToken);

    Channel callChannel(String channelSlackId, Workspace workspace);

    List<Member> findAllWorkspaceMembers();

    void sendMessage(Reminder reminder);
}
