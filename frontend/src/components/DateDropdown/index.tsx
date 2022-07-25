import { getMessagesDate } from "@src/@utils";
import useModal from "@src/hooks/useModal";
import Dimmer from "../@shared/Dimmer";
import DateDropdownMenu from "../DateDropdownMenu";
import DateDropdownToggle from "../DateDropdownToggle";
import * as Styled from "./style";

interface Props {
  postedDate: string;
  channelId: string;
  handleOpenCalendar: () => void;
}

function DateDropdown({ postedDate, channelId, handleOpenCalendar }: Props) {
  const {
    isModalOpened: isDateDropdownOpened,
    handleCloseModal: handleCloseDateDropdown,
    handleToggleModal: handleToggleDateDropdown,
  } = useModal();

  return (
    <>
      {isDateDropdownOpened && (
        <Dimmer hasBackgroundColor={false} onClick={handleCloseDateDropdown} />
      )}
      <Styled.Container>
        <DateDropdownToggle
          text={getMessagesDate(postedDate)}
          onClick={handleToggleDateDropdown}
        />
        {isDateDropdownOpened && (
          <DateDropdownMenu
            date={getMessagesDate(postedDate)}
            channelId={channelId}
            handleOpenCalendar={handleOpenCalendar}
          />
        )}
      </Styled.Container>
    </>
  );
}

export default DateDropdown;
