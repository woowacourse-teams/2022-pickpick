import { useRef, useState } from "react";

type CurrentDays = (string | number)[];

interface ReturnType {
  date: Date;
  handleDecrementMonth: () => void;
  handleIncrementMonth: () => void;
  getCurrentDays: () => CurrentDays;
}

function useCalendar(): ReturnType {
  const date = useRef(new Date());
  const [_, setRerender] = useState(false);
  date.current.setDate(1);

  const handleDecrementMonth = () => {
    setRerender((prev) => !prev);
    date.current.setMonth(date.current.getMonth() - 1);
  };

  const handleIncrementMonth = () => {
    setRerender((prev) => !prev);
    date.current.setMonth(date.current.getMonth() + 1);
  };

  const getCurrentDays = () => {
    const blankCount = date.current.getDay();
    const lastDay = new Date(
      date.current.getFullYear(),
      date.current.getMonth() + 1,
      0
    ).getDate();

    return [
      ...Array.from({ length: blankCount }, () => ""),
      ...Array.from({ length: lastDay }, (_, index) => index + 1),
    ];
  };

  return {
    date: date.current,
    handleDecrementMonth,
    handleIncrementMonth,
    getCurrentDays,
  };
}

export default useCalendar;
