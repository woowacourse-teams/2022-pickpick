import { DATE } from "@src/@constants";
import useModal from "@src/hooks/useModal";
import { useState } from "react";
import Dimmer from "../@shared/Dimmer";
import DropdownMenu from "../DropdownMenu";
import DropdownToggle from "../DropdownToggle";
import * as Styled from "./style";

const DAY: Record<number, string> = {
  0: "일요일",
  1: "월요일",
  2: "화요일",
  3: "수요일",
  4: "목요일",
  5: "금요일",
  6: "토요일",
};

interface Props {
  postedDate: string;
  channelId: string;
  handleOpenCalendar: () => void;
}

function DateDropdown({ postedDate, channelId, handleOpenCalendar }: Props) {
  const {
    isModalOpened: isDateDropdownOpened,
    handleCloseModal: handleCloseDateDropdown,
    handleToggleModal: handleToggleDateDropdown,
  } = useModal();

  const getDateInformation = (givenDate: Date) => {
    const year = givenDate.getFullYear();
    const month = givenDate.getMonth() + 1;
    const date = givenDate.getDate();
    const day = DAY[givenDate.getDay()];

    return { year, month, date, day };
  };

  const getMessagesDate = (postedDate: string): string => {
    const givenDate = getDateInformation(new Date(postedDate));
    const today = getDateInformation(new Date());
    if (
      givenDate.year === today.year &&
      givenDate.month === today.month &&
      givenDate.date === today.date
    )
      return DATE.TODAY;
    if (
      givenDate.year === today.year &&
      givenDate.month === today.month &&
      givenDate.date === today.date - 1
    )
      return DATE.YESTERDAY;

    return `${givenDate.month}월 ${givenDate.date}일 ${givenDate.day}`;
  };

  return (
    <>
      {isDateDropdownOpened && (
        <Dimmer hasBackgroundColor={false} onClick={handleCloseDateDropdown} />
      )}
      <Styled.Container>
        <DropdownToggle
          text={getMessagesDate(postedDate)}
          onClick={handleToggleDateDropdown}
        />
        {isDateDropdownOpened && (
          <DropdownMenu
            date={getMessagesDate(postedDate)}
            channelId={channelId}
            handleOpenCalendar={handleOpenCalendar}
          />
        )}
      </Styled.Container>
    </>
  );
}

export default DateDropdown;
