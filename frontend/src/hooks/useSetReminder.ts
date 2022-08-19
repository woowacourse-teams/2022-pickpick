import { getDateInformation, getMeridiemTime } from "@src/@utils";
import { useEffect, useRef, RefObject, ChangeEventHandler } from "react";
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

interface ReturnType {
  ref: Record<
    | "yearRef"
    | "monthRef"
    | "dateRef"
    | "meridiemRef"
    | "AMHourRef"
    | "PMHourRef"
    | "minuteRef",
    RefObject<HTMLDivElement>
  >;
  checkedState: Record<
    | "checkedMeridiem"
    | "checkedHour"
    | "checkedMinute"
    | "checkedYear"
    | "checkedMonth"
    | "checkedDate",
    string
  >;
  handler: {
    handleChangeMeridiem: ChangeEventHandler<HTMLInputElement>;
    handleChangeHour: ChangeEventHandler<HTMLInputElement>;
    handleChangeMinute: ChangeEventHandler<HTMLInputElement>;
    handleChangeYear: ChangeEventHandler<HTMLInputElement>;
    handleChangeMonth: ChangeEventHandler<HTMLInputElement>;
    handleChangeDate: ChangeEventHandler<HTMLInputElement>;
    handleToggleDateTimePicker: () => void;
  };
}

function useSetReminder({ remindDate }: Props): ReturnType {
  const { year, month, date, hour, minute } = getDateInformation(new Date());
  const { meridiem, hour: meridiemHour } = getMeridiemTime(hour);
  const { parsedHour, parsedMinute } = convertTimeToStepTenMinuteTime({
    hour: Number(meridiemHour),
    minute,
  });

  const yearRef = useRef<HTMLDivElement>(null);
  const monthRef = useRef<HTMLDivElement>(null);
  const dateRef = useRef<HTMLDivElement>(null);

  const meridiemRef = useRef<HTMLDivElement>(null);
  const AMHourRef = useRef<HTMLDivElement>(null);
  const PMHourRef = useRef<HTMLDivElement>(null);
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

    if (AMHourRef.current) {
      AMHourRef.current.scrollTo({
        top: Number(checkedHour) * 22.5,
        behavior: "smooth",
      });
    }

    if (PMHourRef.current) {
      PMHourRef.current.scrollTo({
        top: (Number(checkedHour) === 12 ? 0 : Number(checkedHour)) * 22.5,
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
      AMHourRef,
      PMHourRef,
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
