import { FlexColumn } from "@src/@styles/shared";
import usePushPreviousPage from "@src/hooks/usePushPreviousPage";
import Button from "@src/components/@shared/Button";

function EmptyStatus() {
  const pushPreviousPage = usePushPreviousPage();
  return (
    <FlexColumn gap="30px" margin="25vh 0" alignItems="center">
      <h3>조회된 결과가 없습니다.</h3>
      <Button onClick={pushPreviousPage}>뒤로가기</Button>
    </FlexColumn>
  );
}

export default EmptyStatus;
