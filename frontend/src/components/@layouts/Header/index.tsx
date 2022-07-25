import * as Styled from "./style";

function Header() {
  return (
    <Styled.Container>
      <Styled.Title>줍줍</Styled.Title>
      <a
        href={`https://slack.com/oauth/v2/authorize?scope=users:read&user_scope=identity.basic&redirect_uri=${process.env.SLACK_REDIRECT_URL}&client_id=3740298320131.3743463195250`}
      >
        로그인
      </a>
    </Styled.Container>
  );
}

export default Header;
