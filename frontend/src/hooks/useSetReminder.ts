import { getDateInformation, getMeridiemTime } from "@src/@utils";
import { useEffect, useRef } from "react";
import useDropdown from "./useDropdown";
import useInput from "./useInput";

const convertTimeToStepTenMinuteTime = ({
  hour,
  minute,
}: {
  hour: number;
  minute: number;
}) => {
  if (minute > 50) {
    return { parsedHour: hour + 1, parsedMinute: 0 };
  }

  return { parsedHour: hour, parsedMinute: Math.ceil(minute / 10) * 10 };
};

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

const invalidMeridiem = (value: string) => {
  return value !== "오전" && value !== "오후";
};

interface Props {
  remindDate: string;
}

function useSetReminder({ remindDate }: Props) {
  const { year, month, date, hour, minute } = getDateInformation(new Date());
  const { date: lastDate } = getDateInformation(new Date(year, month, 0));
  const { meridiem, hour: meridiemHour } = getMeridiemTime(hour);
  const { parsedHour, parsedMinute } = convertTimeToStepTenMinuteTime({
    hour: Number(meridiemHour),
    minute,
  });

  const yearRef = useRef<HTMLDivElement>(null);
  const monthRef = useRef<HTMLDivElement>(null);
  const dateRef = useRef<HTMLDivElement>(null);

  const meridiemRef = useRef<HTMLDivElement>(null);
  const hourRef = useRef<HTMLDivElement>(null);
  const minuteRef = useRef<HTMLDivElement>(null);

  const {
    isDropdownOpened: isDateTimePickerOpened,
    handleToggleDropdown: handleToggleDateTimePicker,
  } = useDropdown();

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

  const {
    value: checkedMeridiem,
    handleChangeValue: handleChangeMeridiem,
    changeValue: changeMeridiem,
  } = useInput({ initialValue: meridiem, invalidation: invalidMeridiem });

  const {
    value: checkedHour,
    handleChangeValue: handleChangeHour,
    changeValue: changeHour,
  } = useInput({
    initialValue: parsedHour.toString(),
  });

  const {
    value: checkedMinute,
    handleChangeValue: handleChangeMinute,
    changeValue: changeMinute,
  } = useInput({ initialValue: parsedMinute.toString() });

  const years = [year, year + 1, year + 2].map((year) => year.toString());
  const months = Array.from({ length: 12 }, (_, index) =>
    (index + 1).toString()
  );
  const dates = Array.from({ length: lastDate }, (_, index) =>
    (index + 1).toString()
  );

  const meridiems = ["오전", "오후"];
  const hours = Array.from({ length: 12 }, (_, index) =>
    (index + 1).toString()
  );
  const minutes = Array.from({ length: 6 }, (_, index) =>
    (index * 10).toString()
  );

  useEffect(() => {
    if (remindDate) {
      const { year, month, date, meridiem, meridiemHour, minute } =
        parsedDateTime(remindDate);

      changeYear(year);
      changeMonth(month);
      changeDate(date);

      changeMeridiem(meridiem);
      changeHour(meridiemHour);
      changeMinute(minute);
    }
  }, [remindDate]);

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

    if (meridiemRef.current) {
      meridiemRef.current.scrollTo({
        top: checkedMeridiem === "오전" ? 0 : 22,
        behavior: "smooth",
      });
    }

    if (hourRef.current) {
      hourRef.current.scrollTo({
        top: (Number(checkedHour) - 1) * 22.5,
        behavior: "smooth",
      });
    }

    if (minuteRef.current) {
      minuteRef.current.scrollTo({
        top: (Number(checkedMinute) / 10) * 22.7,
        behavior: "smooth",
      });
    }
  }, [
    checkedMeridiem,
    checkedHour,
    checkedMinute,
    checkedYear,
    checkedMonth,
    checkedDate,
    isDateTimePickerOpened,
  ]);

  return {
    ref: {
      yearRef,
      monthRef,
      dateRef,
      meridiemRef,
      hourRef,
      minuteRef,
    },
    checkedState: {
      checkedMeridiem,
      checkedHour,
      checkedMinute,
      checkedYear,
      checkedMonth,
      checkedDate,
    },
    dateStateArray: {
      meridiems,
      hours,
      minutes,
      years,
      months,
      dates,
    },
    handler: {
      handleChangeMeridiem,
      handleChangeHour,
      handleChangeMinute,
      handleChangeYear,
      handleChangeMonth,
      handleChangeDate,
      handleToggleDateTimePicker,
    },
  };
}

export default useSetReminder;
