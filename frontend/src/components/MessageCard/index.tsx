import ProfileImage from "@src/components/MessageCard/ProfileImage";

import { FlexRow } from "@src/@styles/shared";
import { StrictPropsWithChildren } from "@src/@types/utils";

import * as Styled from "./style";

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
}: StrictPropsWithChildren<Props>) {
  return (
    <Styled.Container role="listItem">
      <FlexRow columnGap="8px" width="100%">
        <ProfileImage src={thumbnail} alt={`${username} 프로필 사진`} />

        <div>
          <FlexRow columnGap="4px" alignItems="center">
            <Styled.Writer aria-label={`작성자는 ${username} 입니다.`}>
              {username}
            </Styled.Writer>
            <Styled.Date
              isHighlighted={isRemindedMessage}
              aria-label={`작성일은 ${date} 입니다.`}
            >
              {date}
            </Styled.Date>
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
