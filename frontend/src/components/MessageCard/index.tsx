import * as Styled from "./style";
import StarIconUnfill from "@public/assets/icons/StarIcon-Unfill.svg";
import AlarmIconActive from "@public/assets/icons/AlarmIcon-Active.svg";
import ProfileImage from "@src/components/ProfileImage";
import { FlexRow } from "@src/@styles/shared";
import { parseTime } from "@src/@utils";
import { useState } from "react";
import IconButton from "../@shared/IconButton";

interface Props {
  username: string;
  date: string;
  text: string;
  thumbnail: string;
  isBookmarked: boolean;
}

function MessageCard({ username, date, text, thumbnail, isBookmarked }: Props) {
  const [isActiveAlarmButton, setIsActiveAlarmButton] = useState(false);
  const [isActiveStarButton, setIsActiveStarButton] = useState(false);

  const handleActiveAlarmButton = () => {
    setIsActiveAlarmButton((prev) => !prev);
  };

  const handleActiveStarButton = () => {
    setIsActiveStarButton((prev) => !prev);
  };

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

      <FlexRow justifyContent="flex-end" alignItems="center" gap="2px">
        <IconButton
          type="button"
          icon="alarm"
          isActive={isActiveAlarmButton}
          onClick={handleActiveAlarmButton}
        >
          <FlexRow justifyContent="center" alignItems="center" gap="5px">
            <Styled.ButtonText>알람설정</Styled.ButtonText>
            <AlarmIconActive width="12px" height="12px" fill="#ffffff" />
          </FlexRow>
        </IconButton>

        <IconButton
          type="button"
          icon="star"
          isActive={isBookmarked}
          onClick={handleActiveStarButton}
        >
          <FlexRow justifyContent="center" alignItems="center" gap="5px">
            <Styled.ButtonText>즐겨찾기</Styled.ButtonText>
            <StarIconUnfill width="12px" height="13.3px" fill="#ffffff" />
          </FlexRow>
        </IconButton>
      </FlexRow>
    </Styled.Container>
  );
}

export default MessageCard;
