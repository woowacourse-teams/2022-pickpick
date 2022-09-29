import { getMessagesDate } from "@src/@utils";
import DateDropdownMenu from "@src/components/DateDropdownMenu";
import DateDropdownToggle from "@src/components/DateDropdownToggle";
import Dropdown from "@src/components/Dropdown";
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
