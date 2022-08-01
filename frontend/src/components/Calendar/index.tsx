import * as Styled from "./style";
import LeftArrowIcon from "@public/assets/icons/ArrowIcon-Left.svg";
import RightArrowIcon from "@public/assets/icons/ArrowIcon-Right.svg";
import useCalendar from "@src/hooks/useCalendar";
import WrapperButton from "../@shared/WrapperButton";

const WEEKDAYS = ["일", "월", "화", "수", "목", "금", "토"] as const;
const MONTHS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12] as const;

function Calendar() {
  const {
    date,
    getCurrentDays,
    isFutureMonth,
    isCurrentMonth,
    handleDecrementMonth,
    handleIncrementMonth,
  } = useCalendar();

  return (
    <Styled.Container>
      <Styled.Month>
        <WrapperButton kind="smallIcon" onClick={handleDecrementMonth}>
          <LeftArrowIcon width="24px" height="24px" fill="#8B8B8B" />
        </WrapperButton>
        <Styled.Title>
          {date.getFullYear()}년 {MONTHS[date.getMonth()]}월
        </Styled.Title>
        <WrapperButton
          kind="smallIcon"
          onClick={handleIncrementMonth}
          isFuture={isFutureMonth()}
          disabled={isFutureMonth()}
        >
          <RightArrowIcon width="24px" height="24px" fill="#8B8B8B" />
        </WrapperButton>
      </Styled.Month>

      <Styled.Weekdays>
        {WEEKDAYS.map((weekDay) => (
          <Styled.Weekday key={weekDay}>{weekDay}</Styled.Weekday>
        ))}
      </Styled.Weekdays>

      <Styled.Days>
        {getCurrentDays().map((day, index) => (
          <Styled.Day
            key={index}
            isBlank={day === ""}
            isCurrentDay={day === new Date().getDate() && isCurrentMonth()}
            isFuture={day > new Date().getDate() && isFutureMonth()}
          >
            {day}
            <div></div>
          </Styled.Day>
        ))}
      </Styled.Days>
    </Styled.Container>
  );
}

export default Calendar;
