import * as Styled from "./style";
import StarIconUnfill from "@public/assets/icons/StarIcon-Unfill.svg";
import AlarmIconActive from "@public/assets/icons/AlarmIcon-Active.svg";
import ProfileImage from "@src/components/ProfileImage";
import { FlexRow } from "@src/@styles/shared";
import { parseTime } from "@src/@utils";
import IconButton from "../@shared/IconButton";
import { PATH_NAME } from "@src/@constants";

interface Props {
  username: string;
  date: string;
  text: string;
  thumbnail: string;
  pathname: string;
  isBookmarked: boolean;
  isSetReminded: boolean;
  toggleBookmark?: () => void;
  handleOpenReminderModal?: () => void;
}

function MessageCard({
  username,
  pathname,
  date,
  text,
  thumbnail,
  isBookmarked,
  isSetReminded,
  toggleBookmark,
  handleOpenReminderModal,
}: Props) {
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
        {pathname !== PATH_NAME.BOOKMARK && (
          <IconButton
            type="button"
            icon="alarm"
            isActive={isSetReminded}
            onClick={handleOpenReminderModal}
          >
            <FlexRow justifyContent="center" alignItems="center" gap="5px">
              <Styled.ButtonText>리마인더</Styled.ButtonText>
              <AlarmIconActive width="12px" height="12px" fill="#ffffff" />
            </FlexRow>
          </IconButton>
        )}

        {pathname !== PATH_NAME.REMINDER && (
          <IconButton
            type="button"
            icon="star"
            isActive={isBookmarked}
            onClick={toggleBookmark}
          >
            <FlexRow justifyContent="center" alignItems="center" gap="5px">
              <Styled.ButtonText>북마크</Styled.ButtonText>
              <StarIconUnfill width="12px" height="13.3px" fill="#ffffff" />
            </FlexRow>
          </IconButton>
        )}
      </FlexRow>
    </Styled.Container>
  );
}

export default MessageCard;
