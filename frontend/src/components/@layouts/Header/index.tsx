import useAuthentication from "@src/hooks/useAuthentication";
import * as Styled from "./style";

function Header() {
  const { login } = useAuthentication();

  return (
    <Styled.Container>
      <Styled.Title>줍줍</Styled.Title>
      <button onClick={login}>로그인</button>
    </Styled.Container>
  );
}

export default Header;
