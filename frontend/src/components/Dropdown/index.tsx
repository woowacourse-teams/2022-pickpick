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
}

function Dropdown({ postedDate }: Props) {
  const [isOpened, setIsOpened] = useState(false);

  const getDateInformation = (givenDate: Date) => {
    const year = givenDate.getFullYear();
    const month = givenDate.getMonth() + 1;
    const date = givenDate.getDate();
    const day = DAY[givenDate.getDay()];

    return { year, month, date, day };
  };

  const handleDropdownToggleClick = () => {
    setIsOpened((prev) => !prev);
  };

  const handleDimmerClick = () => {
    setIsOpened(false);
  };

  const getMessagesDate = () => {
    const givenDate = getDateInformation(new Date(postedDate));
    const today = getDateInformation(new Date());
    if (
      givenDate.year === today.year &&
      givenDate.month === today.month &&
      givenDate.date === today.date
    )
      return "오늘";
    if (
      givenDate.year === today.year &&
      givenDate.month === today.month &&
      givenDate.date === today.date - 1
    )
      return "어제";

    return `${givenDate.month}월 ${givenDate.date}일 ${givenDate.day}`;
  };

  return (
    <>
      {isOpened && (
        <Dimmer hasBackgroundColor={false} onClick={handleDimmerClick} />
      )}
      <Styled.Container>
        <DropdownToggle
          text={getMessagesDate()}
          onClick={handleDropdownToggleClick}
        />
        {isOpened && <DropdownMenu date={getMessagesDate()} />}
      </Styled.Container>
    </>
  );
}

export default Dropdown;
