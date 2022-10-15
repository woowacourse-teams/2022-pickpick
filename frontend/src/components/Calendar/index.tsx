import { Link } from "react-router-dom";
import { useTheme } from "styled-components";

import WrapperButton from "@src/components/@shared/WrapperButton";
import ArrowIconLeft from "@src/components/@svgIcons/ArrowIconLeft";
import ArrowIconRight from "@src/components/@svgIcons/ArrowIconRight";
import useCalendar from "@src/components/Calendar/@hooks/useCalendar";

import { MONTHS, WEEKDAYS } from "@src/@constants/date";
import { ISOConverter } from "@src/@utils/date";

import * as Styled from "./style";

interface Props {
  channelId: string;
  handleCloseCalendar: VoidFunction;
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

  const theme = useTheme();

  return (
    <Styled.Container>
      <Styled.Month>
        <WrapperButton kind="smallIcon" onClick={handleDecrementMonth}>
          <ArrowIconLeft
            width="24px"
            height="24px"
            fill={theme.COLOR.SECONDARY.DEFAULT}
          />
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
          <ArrowIconRight
            width="24px"
            height="24px"
            fill={theme.COLOR.SECONDARY.DEFAULT}
          />
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

          if (isBlank || isFuture) {
            return (
              <Styled.Day
                isBlank={isBlank}
                isCurrentDay={isCurrentDay}
                isFuture={isFuture}
                onClick={handleCloseCalendar}
              >
                {day}
                <div></div>
              </Styled.Day>
            );
          }

          return (
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
