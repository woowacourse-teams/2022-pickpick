package com.pickpick.fixture;

import static com.pickpick.fixture.WorkspaceFixture.JUPJUP;

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
public class FakeClientFixture {

    Map<String, List<Channel>> memberParticipatingChannels = new HashMap<>();

    static Workspace jupjup = JUPJUP.create();

    public static Map<String, Workspace> codeAndWorkspace = Arrays.stream(MemberFixture.values())
            .map(memberFixture -> memberFixture.createLogin(jupjup))
            .collect(Collectors.toMap(member -> member.getSlackId() + "code", member -> jupjup));

    public static Map<String, Member> codeAndMember = Arrays.stream(MemberFixture.values())
            .map(memberFixture -> memberFixture.createLogin(jupjup))
            .collect(Collectors.toMap(member -> member.getSlackId() + "code", member -> member));

    public static Map<String, Member> tokenAndMember = Arrays.stream(MemberFixture.values())
            .map(memberFixture -> memberFixture.createLogin(jupjup))
            .collect(Collectors.toMap(Member::getToken, member -> member));

    public static Map<String, Channel> slackIdAndChannel = Arrays.stream(ChannelFixture.values())
            .map(channelFixture -> channelFixture.create(jupjup))
            .collect(Collectors.toMap(Channel::getSlackId, channel -> channel));

    public static String getRandomMemberCode() {
        return codeAndMember.keySet()
                .stream()
                .findFirst()
                .orElse(MemberFixture.values()[0].getSlackId() + "code");
    }

    public static String getMemberSlackIdByCode(String code) {
        return codeAndMember.get(code).getSlackId();
    }

    public static int getDefaultChannelSize() {
        return ChannelFixture.values().length;
    }

    public void setParticipatingChannel(Member member, Channel... channels) {
        memberParticipatingChannels.put(member.getToken(), List.of(channels));
    }

    public void setParticipatingChannel(String memberCode, List<Channel> channels) {
        Member member = codeAndMember.get(memberCode);
        memberParticipatingChannels.put(member.getToken(), channels);
    }

    public Map<String, List<Channel>> getMemberParticipatingChannels() {
        return memberParticipatingChannels;
    }
}
