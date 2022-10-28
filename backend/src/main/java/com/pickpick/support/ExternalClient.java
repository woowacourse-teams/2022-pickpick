package com.pickpick.support;

import com.pickpick.auth.application.dto.MemberInfoDto;
import com.pickpick.auth.application.dto.OAuthAccessInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import com.pickpick.message.domain.Reminder;
import com.pickpick.slackevent.domain.Participation;
import com.pickpick.workspace.domain.Workspace;
import java.util.List;

public interface ExternalClient {

    OAuthAccessInfoDto callOAuthAccessInfo(String code);

    MemberInfoDto callMemberInfo(String code);

    List<Member> findMembersByWorkspace(Workspace workspace);

    List<Channel> findChannelsByWorkspace(Workspace workspace);

    Participation findChannelParticipation(String userToken);

    void sendMessage(Reminder reminder);

    void inviteBotToChannel(Member member, Channel channel);
}
