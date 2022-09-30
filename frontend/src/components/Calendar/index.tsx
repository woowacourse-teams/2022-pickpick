import * as Styled from "./style";
import ArrowIconRight from "@src/components/@svgIcons/ArrowIconRight";
import ArrowIconLeft from "@src/components/@svgIcons/ArrowIconLeft";
import useCalendar from "@src/hooks/useCalendar";
import WrapperButton from "@src/components/@shared/WrapperButton";
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
    todayDate,
    firstOfMonthDate,
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
          <ArrowIconLeft width="24px" height="24px" fill="#8B8B8B" />
        </WrapperButton>

        <Styled.Title>
          {`${firstOfMonthDate.getFullYear()}년 ${
            MONTHS[firstOfMonthDate.getMonth()]
          }월`}
        </Styled.Title>

        <WrapperButton
          kind="smallIcon"
          onClick={handleIncrementMonth}
          isFuture={isFutureMonth()}
          disabled={isFutureMonth()}
        >
          <ArrowIconRight width="24px" height="24px" fill="#8B8B8B" />
        </WrapperButton>
      </Styled.Month>

      <Styled.Weekdays>
        {WEEKDAYS.map((weekDay) => (
          <Styled.Weekday key={weekDay}>{weekDay}</Styled.Weekday>
        ))}
      </Styled.Weekdays>

      <Styled.Days>
        {getCurrentDays().map((day, index) => {
          const isBlank = day === "";
          const isFuture = day > todayDate.getDate() && isFutureMonth();
          const isCurrentDay = day === todayDate.getDate() && isCurrentMonth();

          return isBlank || isFuture ? (
            <Styled.Day
              isBlank={isBlank}
              isCurrentDay={isCurrentDay}
              isFuture={isFuture}
              onClick={handleCloseCalendar}
            >
              {day}
              <div></div>
            </Styled.Day>
          ) : (
            <Link
              key={index}
              to={`/feed/${channelId}/${ISOConverter(
                `${todayDate.getFullYear()}-${
                  MONTHS[todayDate.getMonth()]
                }-${day}`
              )}`}
            >
              <Styled.Day
                isBlank={isBlank}
                isCurrentDay={isCurrentDay}
                isFuture={isFuture}
                onClick={handleCloseCalendar}
              >
                {day}
                <div></div>
              </Styled.Day>
            </Link>
          );
        })}
      </Styled.Days>
    </Styled.Container>
  );
}

export default Calendar;
