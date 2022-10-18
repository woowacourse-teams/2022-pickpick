import { FlexColumn } from "@src/@styles/shared";

import * as Styled from "./style";

function MessageCardSkeleton() {
  return (
    <Styled.Container aria-hidden>
      <Styled.ProfileImageSkeleton />
      <FlexColumn width="100%" gap="4px">
        <Styled.WriterSkeleton />
        <Styled.LongLineSkeleton />
        <Styled.LongLineSkeleton />
        <Styled.LongLineSkeleton />
        <Styled.ShortLineSkeleton />
      </FlexColumn>
    </Styled.Container>
  );
}

export default MessageCardSkeleton;
