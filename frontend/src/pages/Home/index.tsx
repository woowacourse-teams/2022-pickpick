import LogoIcon from "@public/assets/icons/pickpick.svg";

import Button from "@src/components/@shared/Button";

import { SLACK_URL } from "@src/@constants/path";
import { FlexRow, SrOnlyTitle } from "@src/@styles/shared";

import * as Styled from "./style";

function Home() {
  const handleNavigateToRegisterWorkspace = () => {
    location.href = SLACK_URL.REGISTER_WORKSPACE;
  };

  const handleNavigateToLogin = () => {
    location.href = SLACK_URL.LOGIN;
  };

  return (
    <Styled.Container>
      <SrOnlyTitle>줍줍 시작하기</SrOnlyTitle>

      <Styled.GreetingContainer>
        <FlexRow
          gap="30px"
          marginBottom="27px"
          justifyContent="center"
          alignItems="center"
          flexWrap="wrap"
        >
          <LogoIcon width="200px" height="200px" />
          <h2>
            사라지는 슬랙 메시지,
            <br />
            우리가 주워줄게!
          </h2>
        </FlexRow>

        <FlexRow
          gap="10px"
          flexWrap="wrap"
          justifyContent="center"
          alignItems="center"
        >
          <Button onClick={handleNavigateToRegisterWorkspace}>
            워크스페이스 등록
          </Button>
          <Button onClick={handleNavigateToLogin} styleType="secondary">
            로그인
          </Button>
        </FlexRow>
      </Styled.GreetingContainer>

      <Styled.UsageContainer>
        <h1>⚠️ 주의사항</h1>

        <Styled.UsageList>
          <li>보안이 중요한 워크스페이스라면 설치를 권장하지 않아요.</li>
          <li>
            워크스페이스 참여자 전원의 동의를 받고 줍줍 워크스페이스를
            등록해주세요!
          </li>
        </Styled.UsageList>
      </Styled.UsageContainer>

      <Styled.UsageContainer>
        <h1>🌱 새로운 워크스페이스를 등록하실건가요?</h1>

        <Styled.UsageOrderedList>
          <li>
            워크스페이스 등록을 클릭하면 줍줍 슬랙 앱이 해당 워크스페이스에 설치
            되어요.
          </li>
          <li>
            줍줍이 설치된 채널의 메시지가 백업 되어요. (설치 이전 메시지는
            백업되지 않아요 👀)
          </li>
          <li>메시지를 확인하시려면, 채널을 구독해주세요.</li>
        </Styled.UsageOrderedList>
      </Styled.UsageContainer>

      <Styled.UsageContainer>
        <h1>🌻 이미 워크스페이스를 등록하셨나요?</h1>

        <Styled.UsageList>
          <li>
            줍줍이 설치 된 워크스페이스에 로그인 해서 백업된 메시지를
            확인해주세요!
          </li>
        </Styled.UsageList>
      </Styled.UsageContainer>
    </Styled.Container>
  );
}

export default Home;
