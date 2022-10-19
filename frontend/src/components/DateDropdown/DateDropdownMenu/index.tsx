import DateDropdownOption from "@src/components/DateDropdown/DateDropdownOption";

import * as Styled from "./style";

interface Props {
  date: string;
  channelId: string;
  handleOpenCalendar: VoidFunction;
}

function DateDropdownMenu({ date, channelId, handleOpenCalendar }: Props) {
  return (
    <Styled.Container>
      <DateDropdownOption channelId={channelId} date={date} />

      <hr />

      <Styled.Option>
        <Styled.Button type="button" onClick={handleOpenCalendar} tabIndex={0}>
          특정 날짜로 이동
        </Styled.Button>
      </Styled.Option>
    </Styled.Container>
  );
}

export default DateDropdownMenu;
