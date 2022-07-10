import * as Styled from "./style";

import StarIconFill from "@public/assets/icons/StarIcon-Fill.svg";
import AlarmIconActive from "@public/assets/icons/AlarmIcon-Active.svg";
import RemoveIcon from "@public/assets/icons/RemoveIcon.svg";

import ProfileImage from "@src/components/ProfileImage";
import WrapperButton from "@src/components/@shared/WrapperButton";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import { parseTime } from "@src/@utils";

interface Props {
  username: string;
  date: string;
  text: string;
  thumbnail: string;
}

function MessageCard({ username, date, text, thumbnail }: Props) {
  return (
    <Styled.Container>
      <FlexRow columnGap="8px" width="100%">
        <ProfileImage src={thumbnail} alt={`${username} 프로필 사진`} />
        <div>
          <FlexRow columnGap="4px" alignItems="center">
            <Styled.Writer>{username}</Styled.Writer>
            <Styled.Date>{parseTime(date)}</Styled.Date>
          </FlexRow>
          <Styled.Message>{text}</Styled.Message>
        </div>
      </FlexRow>
      <FlexColumn alignItems="center">
        <WrapperButton kind="smallIcon">
          <RemoveIcon width="12px" height="12px" fill="#8B8B8B" />
        </WrapperButton>
        <WrapperButton kind="smallIcon">
          <AlarmIconActive width="12px" height="12px" fill="#0742FA" />
        </WrapperButton>
        <WrapperButton kind="smallIcon">
          <StarIconFill width="12px" height="13.3px" fill="#FFAD33" />
        </WrapperButton>
      </FlexColumn>
    </Styled.Container>
  );
}

export default MessageCard;
