import { PropsWithChildren } from "react";
import * as Styled from "./style";
import ProfileImage from "@src/components/ProfileImage";
import { FlexRow } from "@src/@styles/shared";

interface Props {
  username: string;
  date: string;
  text: string;
  thumbnail: string;
  isRemindedMessage: boolean;
}

function MessageCard({
  username,
  date,
  text,
  thumbnail,
  isRemindedMessage,
  children,
}: PropsWithChildren<Props>) {
  return (
    <Styled.Container>
      <FlexRow columnGap="8px" width="100%">
        <ProfileImage src={thumbnail} alt={`${username} 프로필 사진`} />
        <div>
          <FlexRow columnGap="4px" alignItems="center">
            <Styled.Writer>{username}</Styled.Writer>
            <Styled.Date isHighlighted={isRemindedMessage}>{date}</Styled.Date>
          </FlexRow>
          <Styled.Message>{text}</Styled.Message>
        </div>
      </FlexRow>

      <FlexRow justifyContent="flex-end" alignItems="center" gap="2px">
        {children}
      </FlexRow>
    </Styled.Container>
  );
}

export default MessageCard;
