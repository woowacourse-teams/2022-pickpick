import { PATH_NAME } from "@src/@constants";
import Header from "@src/components/@layouts/Header";
import Button from "@src/components/@shared/Button";
import * as LayoutStyled from "@src/components/@layouts/LayoutContainer/style";
import * as Styled from "./style";
import { FlexColumn } from "@src/@styles/shared";

function UnexpectedError() {
  return (
    <LayoutStyled.Container>
      <Header />

      <LayoutStyled.Main hasMarginTop={true}>
        <FlexColumn gap="30px" margin="25vh 0" alignItems="center">
          <Styled.Title>예상하지 못한 에러가 발생했습니다.</Styled.Title>

          <Styled.Description>
            피드로 돌아가시려면 아래 버튼을 클릭해주세요.
          </Styled.Description>

          <a href={PATH_NAME.FEED}>
            <Button>메인 피드로 가기</Button>
          </a>
        </FlexColumn>
      </LayoutStyled.Main>
    </LayoutStyled.Container>
  );
}

export default UnexpectedError;
