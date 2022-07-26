package com.pickpick.acceptance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.pickpick.member.domain.Member;
import com.pickpick.member.domain.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("유저 기능")
@SuppressWarnings("NonAsciiCharacters")
public class MemberAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 프로젝트_기동_시점에_유저가_저장되어_있어야_한다() {
        List<Member> members = memberRepository.findAll();
        assertThat(members.isEmpty()).isFalse();
    }
}
