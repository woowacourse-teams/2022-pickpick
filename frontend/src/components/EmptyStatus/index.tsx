import Button from "@src/components/@shared/Button";

import usePushPreviousPage from "@src/hooks/@shared/usePushPreviousPage";

import * as Styled from "./style";

function EmptyStatus() {
  const pushPreviousPage = usePushPreviousPage();

  return (
    <Styled.Container>
      <h1>조회된 결과가 없습니다.</h1>

      <Button type="button" onClick={pushPreviousPage}>
        뒤로가기
      </Button>
    </Styled.Container>
  );
}

export default EmptyStatus;
