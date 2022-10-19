import Dropdown from "@src/components/@shared/Dropdown";
import DateDropdownMenu from "@src/components/DateDropdown/DateDropdownMenu";
import DateDropdownToggle from "@src/components/DateDropdown/DateDropdownToggle";

import { getMessagesDate } from "@src/@utils/date";

import * as Styled from "./style";

interface Props {
  postedDate: string;
  channelId: string;
  handleOpenCalendar: VoidFunction;
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
