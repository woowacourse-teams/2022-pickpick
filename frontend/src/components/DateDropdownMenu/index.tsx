import DateDropdownOption from "../DateDropdownOption";
import * as Styled from "./style";

interface Props {
  date: string;
  channelId: string;
  handleOpenCalendar: () => void;
}

function DateDropdownMenu({ date, channelId, handleOpenCalendar }: Props) {
  return (
    <Styled.Container>
      <DateDropdownOption channelId={channelId} date={date} />
      <hr />
      <Styled.Option>
        <Styled.Button type="button" onClick={handleOpenCalendar}>
          특정 날짜로 이동
        </Styled.Button>
      </Styled.Option>
    </Styled.Container>
  );
}

export default DateDropdownMenu;
