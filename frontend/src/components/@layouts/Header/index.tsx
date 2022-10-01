import { SLACK_LOGIN_URL } from "@src/@constants";

import * as Styled from "./style";

function Header() {
  return (
    <Styled.Container>
      <Styled.Title>줍줍</Styled.Title>
      <a href={SLACK_LOGIN_URL}>로그인</a>
    </Styled.Container>
  );
}

export default Header;
