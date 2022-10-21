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
        <h1>이용 방법</h1>

        <Styled.UsageList>
          <li>
            워크스페이스에 줍줍 Slack App 을 설치하고 백업하고 싶은 채널에
            초대해주세요 🤗
          </li>
          <li>이제부터 여러분의 대화를 줍줍이가 보관해드릴거예요 🤚</li>
          <li>사이트에 방문하셔서 대화를 확인해보세요 😎</li>
        </Styled.UsageList>
      </Styled.UsageContainer>
    </Styled.Container>
  );
}

export default Home;
