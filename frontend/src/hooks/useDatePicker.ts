import { getDateInformation, getMeridiemTime } from "@src/@utils";
import {
  useEffect,
  useRef,
  RefObject,
  ChangeEventHandler,
  useState,
} from "react";
import useInput from "./useInput";

const parsedDateTime = (ISODateTime: string) => {
  const [fullDate, fullTime] = ISODateTime.split("T");
  const [year, month, date] = fullDate.split("-");
  const [hour, minute] = fullTime.split(":");
  const { meridiem: meridiem, hour: meridiemHour } = getMeridiemTime(
    Number(hour)
  );

  return {
    year,
    month,
    date,
    meridiem,
    meridiemHour,
    minute,
  };
};

interface Props {
  remindDate: string;
}

function useDatePicker({ remindDate }: Props) {
  const { year, month, date, hour, minute } = getDateInformation(new Date());
  const yearRef = useRef<HTMLDivElement>(null);
  const monthRef = useRef<HTMLDivElement>(null);
  const dateRef = useRef<HTMLDivElement>(null);

  const [dateDropdownFlag, setDateDropdownFlag] = useState(false);

  const {
    value: checkedYear,
    handleChangeValue: handleChangeYear,
    changeValue: changeYear,
  } = useInput({
    initialValue: year.toString(),
  });

  const {
    value: checkedMonth,
    handleChangeValue: handleChangeMonth,
    changeValue: changeMonth,
  } = useInput({ initialValue: month.toString() });

  const {
    value: checkedDate,
    handleChangeValue: handleChangeDate,
    changeValue: changeDate,
  } = useInput({
    initialValue: date.toString(),
  });

  useEffect(() => {
    if (yearRef.current) {
      yearRef.current.scrollTo({
        top: (Number(checkedYear) - year) * 22,
        behavior: "smooth",
      });
    }

    if (monthRef.current) {
      monthRef.current.scrollTo({
        top: (Number(checkedMonth) - 1) * 22.5,
        behavior: "smooth",
      });
    }

    if (dateRef.current) {
      dateRef.current.scrollTo({
        top: (Number(checkedDate) - 1) * 22.5,
        behavior: "smooth",
      });
    }
  }, [checkedYear, checkedMonth, checkedDate, dateDropdownFlag]);

  const handleResetDatePickerPosition = () => {
    setDateDropdownFlag((prev) => !prev);
  };

  useEffect(() => {
    if (remindDate) {
      const { year, month, date, meridiem, meridiemHour, minute } =
        parsedDateTime(remindDate);

      changeYear(year);
      changeMonth(month);
      changeDate(date);
    }
  }, [remindDate]);

  return {
    yearRef,
    monthRef,
    dateRef,
    checkedYear,
    checkedMonth,
    checkedDate,
    handleChangeYear,
    handleChangeMonth,
    handleChangeDate,
    handleResetDatePickerPosition,
  };
}

export default useDatePicker;
