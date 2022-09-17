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

const invalidMeridiem = (value: string) => {
  return value !== "오전" && value !== "오후";
};

interface Props {
  remindDate: string;
}

function useDatePicker({ remindDate }: Props) {
  const { year, month, date, hour, minute } = getDateInformation(new Date());
  const { meridiem, hour: meridiemHour } = getMeridiemTime(hour);
  const { parsedHour, parsedMinute } = convertTimeToStepTenMinuteTime({
    hour: Number(meridiemHour),
    minute,
  });

  const meridiemRef = useRef<HTMLDivElement>(null);
  const AMHourRef = useRef<HTMLDivElement>(null);
  const PMHourRef = useRef<HTMLDivElement>(null);
  const minuteRef = useRef<HTMLDivElement>(null);

  const [timeDropdownFlag, setTimeDropdownFlag] = useState(false);

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

  const handleResetTimePickerPosition = () => {
    setTimeDropdownFlag((prev) => !prev);
  };

  useEffect(() => {
    if (remindDate) {
      const { meridiem, meridiemHour, minute } = parsedDateTime(remindDate);
      changeMeridiem(meridiem);
      changeHour(meridiemHour);
      changeMinute(minute);
    }
  }, [remindDate]);

  useEffect(() => {
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
  }, [checkedMeridiem, checkedHour, checkedMinute, timeDropdownFlag]);

  return {
    meridiemRef,
    AMHourRef,
    PMHourRef,
    minuteRef,
    checkedMeridiem,
    checkedHour,
    checkedMinute,
    handleChangeMeridiem,
    handleChangeHour,
    handleChangeMinute,
    handleResetTimePickerPosition,
  };
}

export default useDatePicker;
