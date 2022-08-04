import * as Styled from "./style";
import LeftArrowIcon from "@public/assets/icons/ArrowIcon-Left.svg";
import RightArrowIcon from "@public/assets/icons/ArrowIcon-Right.svg";
import useCalendar from "@src/hooks/useCalendar";
import WrapperButton from "../@shared/WrapperButton";
import { Link } from "react-router-dom";
import { ISOConverter } from "@src/@utils";

const WEEKDAYS = ["일", "월", "화", "수", "목", "금", "토"] as const;
const MONTHS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12] as const;

interface Props {
  channelId: string;
  handleCloseCalendar: () => void;
}

function Calendar({ channelId, handleCloseCalendar }: Props) {
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
          <Link
            key={index}
            to={`/feed/${channelId}/${ISOConverter(
              `${date.getFullYear()}-${MONTHS[date.getMonth()]}-${day}`
            )}`}
          >
            <Styled.Day
              isBlank={day === ""}
              isCurrentDay={day === new Date().getDate() && isCurrentMonth()}
              isFuture={day > new Date().getDate() && isFutureMonth()}
              onClick={handleCloseCalendar}
            >
              {day}
              <div></div>
            </Styled.Day>
          </Link>
        ))}
      </Styled.Days>
    </Styled.Container>
  );
}

export default Calendar;
