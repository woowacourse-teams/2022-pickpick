import { SLACK_URL } from "@src/@constants/path";

import * as Styled from "./style";

function Header() {
  return (
    <Styled.Container>
      <Styled.Title>줍줍</Styled.Title>
      <a href={SLACK_URL.LOGIN}>로그인</a>
    </Styled.Container>
  );
}

export default Header;
