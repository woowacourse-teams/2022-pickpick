import { Link } from "react-router-dom";
import { useTheme } from "styled-components";

import WrapperButton from "@src/components/@shared/WrapperButton";
import ArrowIconLeft from "@src/components/@svgIcons/ArrowIconLeft";
import ArrowIconRight from "@src/components/@svgIcons/ArrowIconRight";
import useCalendar from "@src/components/Calendar/@hooks/useCalendar";

import { MONTHS, WEEKDAYS } from "@src/@constants/date";
import { SrOnlyTitle } from "@src/@styles/shared";
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
      <SrOnlyTitle>특정 날짜로 이동</SrOnlyTitle>

      <Styled.Month>
        <WrapperButton
          kind="smallIcon"
          onClick={handleDecrementMonth}
          aria-label="이전 달 보기"
        >
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
          disabled={isFutureMonth()}
          aria-label="다음 달 보기"
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

      <Styled.Days role="list">
        {getCurrentDays().map((day, index) => {
          const today = todayDate.getDate();

          const isBlank = day === "";
          const isFuture = day > today && isFutureMonth();
          const isToday = day === today && isCurrentMonth();

          if (isBlank || isFuture) {
            return (
              <Styled.Day
                isBlank={isBlank}
                isToday={isToday}
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
                `${firstOfMonthDate.getFullYear()}-${
                  MONTHS[firstOfMonthDate.getMonth()]
                }-${day}`
              )}`}
              role="listItem"
              aria-label={`${firstOfMonthDate.getFullYear()}년 ${
                MONTHS[firstOfMonthDate.getMonth()]
              }월 ${day}일 로 이동`}
            >
              <Styled.Day
                isBlank={isBlank}
                isToday={isToday}
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
