import { getMessagesDate } from "@src/@utils";
import useModal from "@src/hooks/useModal";
import Dimmer from "../@shared/Dimmer";
import DropdownMenu from "../DropdownMenu";
import DropdownToggle from "../DropdownToggle";
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
        <DropdownToggle
          text={getMessagesDate(postedDate)}
          onClick={handleToggleDateDropdown}
        />
        {isDateDropdownOpened && (
          <DropdownMenu
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
