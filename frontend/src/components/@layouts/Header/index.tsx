import WrapperButton from "@src/components/@shared/WrapperButton";
import * as Styled from "./style";

function Header() {
  return (
    <Styled.Container>
      <Styled.Title>줍줍</Styled.Title>
      <WrapperButton kind="text">로그인</WrapperButton>
    </Styled.Container>
  );
}

export default Header;
