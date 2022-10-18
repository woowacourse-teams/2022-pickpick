package com.pickpick.fixture;

import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;

import com.pickpick.auth.application.dto.WorkspaceInfoDto;
import com.pickpick.channel.domain.Channel;
import com.pickpick.member.domain.Member;
import com.pickpick.workspace.domain.Workspace;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StubSlack {

    private final Workspace jupjup = JUPJUP.create();

    private final Map<String, List<Channel>> memberParticipatingChannels = new HashMap<>();

    private final Map<String, Workspace> codeAndWorkspace = Arrays.stream(MemberFixture.values())
            .map(memberFixture -> memberFixture.createLogin(jupjup))
            .collect(Collectors.toMap(member -> member.getSlackId() + "code", member -> jupjup));

    private final Map<String, Member> codeAndMember = Arrays.stream(MemberFixture.values())
            .map(memberFixture -> memberFixture.createLogin(jupjup))
            .collect(Collectors.toMap(member -> member.getSlackId() + "code", member -> member));

    private final Map<String, Member> tokenAndMember = Arrays.stream(MemberFixture.values())
            .map(memberFixture -> memberFixture.createLogin(jupjup))
            .collect(Collectors.toMap(Member::getToken, member -> member));

    public String callUserToken(final String code) {
        return codeAndMember.get(code).getToken();
    }

    public WorkspaceInfoDto callWorkspaceInfo(final String code) {
        Workspace workspace = codeAndWorkspace.get(code);
        return new WorkspaceInfoDto(workspace.getSlackId(), workspace.getBotToken(), workspace.getBotSlackId());
    }

    public String callMemberSlackId(final String userToken) {
        return tokenAndMember.get(userToken).getSlackId();
    }

    public String getRandomMemberCode() {
        return codeAndMember.keySet()
                .stream()
                .findFirst()
                .orElse(MemberFixture.values()[0].getSlackId() + "code");
    }

    public String getMemberSlackIdByCode(String code) {
        return codeAndMember.get(code).getSlackId();
    }

    public void setParticipatingChannel(Member member, Channel... channels) {
        memberParticipatingChannels.put(member.getToken(), List.of(channels));
    }

    public void setParticipatingChannel(String memberCode, List<Channel> channels) {
        Member member = codeAndMember.get(memberCode);
        memberParticipatingChannels.put(member.getToken(), channels);
    }

    public List<Channel> getParticipatingChannels(final String userToken) {
        Member member = tokenAndMember.get(userToken);
        return memberParticipatingChannels.get(member.getToken());
    }
}
