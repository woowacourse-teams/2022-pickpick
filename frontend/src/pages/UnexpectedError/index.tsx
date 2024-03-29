import Header from "@src/components/@layouts/Header";
import * as LayoutStyled from "@src/components/@layouts/LayoutContainer/style";
import Button from "@src/components/@shared/Button";

import { PATH_NAME } from "@src/@constants/path";
import { FlexColumn } from "@src/@styles/shared";

import * as Styled from "./style";

function UnexpectedError() {
  return (
    <div>
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
    </div>
  );
}

export default UnexpectedError;
