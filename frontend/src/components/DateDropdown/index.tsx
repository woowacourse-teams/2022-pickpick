import { getMessagesDate } from "@src/@utils";
import Dropdown from "@src/components/Dropdown";
import DateDropdownToggle from "@src/components/DateDropdown/DateDropdownToggle";
import DateDropdownMenu from "@src/components/DateDropdown/DateDropdownMenu";
import * as Styled from "./style";

interface Props {
  postedDate: string;
  channelId: string;
  handleOpenCalendar: () => void;
}

function DateDropdown({ postedDate, channelId, handleOpenCalendar }: Props) {
  return (
    <Dropdown>
      {({ innerRef, isDropdownOpened, handleToggleDropdown }) => {
        return (
          <Styled.Container ref={innerRef}>
            <DateDropdownToggle
              text={getMessagesDate(postedDate)}
              onClick={handleToggleDropdown}
            />
            {isDropdownOpened && (
              <DateDropdownMenu
                date={getMessagesDate(postedDate)}
                channelId={channelId}
                handleOpenCalendar={handleOpenCalendar}
              />
            )}
          </Styled.Container>
        );
      }}
    </Dropdown>
  );
}

export default DateDropdown;
