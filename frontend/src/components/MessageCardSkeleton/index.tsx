import { FlexColumn } from "@src/@styles/shared";
import * as Styled from "./style";

function MessageCardSkeleton() {
  return (
    <Styled.Container>
      <Styled.ProfileImageSkeleton />
      <FlexColumn width="100%" gap="4px">
        <Styled.WriterSkeleton />
        <Styled.FirstLineSkeleton />
        <Styled.SecondLineSkeleton />
      </FlexColumn>
    </Styled.Container>
  );
}

export default MessageCardSkeleton;
