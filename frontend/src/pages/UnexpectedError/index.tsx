import { PATH_NAME } from "@src/@constants";
import Footer from "@src/components/@layouts/Footer";
import Header from "@src/components/@layouts/Header";
import Button from "@src/components/@shared/Button";
import * as Styled from "@src/components/@layouts/LayoutContainer/style";
import { FlexColumn } from "@src/@styles/shared";

function UnexpectedError() {
  return (
    <Styled.Container>
      <Header />
      <Styled.Main hasMarginTop={true}>
        <FlexColumn gap="30px" margin="30vh 0">
          <h1>예상치 못한 에러 발생</h1>
          <a href={PATH_NAME.FEED}>
            <Button>메인 피드로 가기</Button>
          </a>
        </FlexColumn>
      </Styled.Main>
      <Footer />
    </Styled.Container>
  );
}

export default UnexpectedError;
