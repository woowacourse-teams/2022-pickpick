import { PATH_NAME } from "@src/@constants";
import Footer from "@src/components/@layouts/Footer";
import Header from "@src/components/@layouts/Header";
import * as Styled from "@src/components/@layouts/LayoutContainer/style";

function UnexpectedError() {
  return (
    <Styled.Container>
      <Header />
      <Styled.Main hasMarginTop={true}>
        <h1>예상치 못한 에러 발생</h1>
        <a href={PATH_NAME.FEED}>
          <button>메인 피드로 가기</button>
        </a>
      </Styled.Main>
      <Footer />
    </Styled.Container>
  );
}

export default UnexpectedError;
