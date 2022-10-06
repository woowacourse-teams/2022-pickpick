import { useRef, useState } from "react";

type CurrentDays = (string | number)[];

interface UseCalendarResult {
  todayDate: Date;
  firstOfMonthDate: Date;
  isFutureMonth: () => boolean;
  isCurrentMonth: () => boolean;
  handleDecrementMonth: () => void;
  handleIncrementMonth: () => void;
  getCurrentDays: () => CurrentDays;
}

function useCalendar(): UseCalendarResult {
  const firstOfMonthDate = useRef(new Date());
  const todayDate = useRef(new Date());
  const [_, setRerender] = useState(false);
  firstOfMonthDate.current.setDate(1);

  const isFutureMonth = () =>
    firstOfMonthDate.current.getFullYear() >= todayDate.current.getFullYear() &&
    firstOfMonthDate.current.getMonth() >= todayDate.current.getMonth();

  const isCurrentMonth = () =>
    firstOfMonthDate.current.getFullYear() ===
      todayDate.current.getFullYear() &&
    firstOfMonthDate.current.getMonth() === todayDate.current.getMonth();

  const handleDecrementMonth = () => {
    setRerender((prev) => !prev);
    firstOfMonthDate.current.setMonth(firstOfMonthDate.current.getMonth() - 1);
  };

  const handleIncrementMonth = () => {
    setRerender((prev) => !prev);
    firstOfMonthDate.current.setMonth(firstOfMonthDate.current.getMonth() + 1);
  };

  const getCurrentDays = () => {
    const blankCount = firstOfMonthDate.current.getDay();
    const lastDay = new Date(
      firstOfMonthDate.current.getFullYear(),
      firstOfMonthDate.current.getMonth() + 1,
      0
    ).getDate();

    return [
      ...Array.from({ length: blankCount }, () => ""),
      ...Array.from({ length: lastDay }, (_, index) => index + 1),
    ];
  };

  return {
    todayDate: todayDate.current,
    firstOfMonthDate: firstOfMonthDate.current,
    isFutureMonth,
    isCurrentMonth,
    handleDecrementMonth,
    handleIncrementMonth,
    getCurrentDays,
  };
}

export default useCalendar;
