import * as Styled from "./style";
import Button from "@src/components/@shared/Button";
import { FlexRow } from "@src/@styles/shared";
import LogoIcon from "@public/assets/icons/pickpick.svg";

function Home() {
  const handleNavigateToAddChannel = () => {
    location.href = `https://slack.com/oauth/v2/authorize?scope=users:read&user_scope=identity.basic&redirect_uri=${process.env.SLACK_REDIRECT_URL}&client_id=3740298320131.3743463195250`;
  };

  return (
    <Styled.Container>
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
        <Button onClick={handleNavigateToAddChannel}>시작하기</Button>
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
