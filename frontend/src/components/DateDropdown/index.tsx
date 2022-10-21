import Dropdown from "@src/components/@shared/Dropdown";
import DateDropdownMenu from "@src/components/DateDropdown/DateDropdownMenu";
import DateDropdownToggle from "@src/components/DateDropdown/DateDropdownToggle";

import { SrOnlyDescription } from "@src/@styles/shared";
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
              aria-describedby="description"
            />
            <SrOnlyDescription id="description">
              {isDropdownOpened
                ? "드랍다운 메뉴가 열려있습니다. 드랍다운 메뉴를 닫으려면 클릭해주세요."
                : "드랍다운 메뉴가 닫혀있습니다. 드랍다운 메뉴를 열려면 클릭해주세요."}
            </SrOnlyDescription>

            <SrOnlyDescription aria-live="assertive">
              {isDropdownOpened
                ? "드랍다운 메뉴가 열렸습니다."
                : "드랍다운 메뉴가 닫혔습니다."}
            </SrOnlyDescription>

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
