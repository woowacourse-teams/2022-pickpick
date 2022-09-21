import { PICKER_OPTION_SCROLL } from "@src/@constants";
import { getDateInformation, getMeridiemTime } from "@src/@utils";
import { useEffect, useRef, useState } from "react";
import useInput from "./useInput";

const parseTime = (ISODateTime: string) => {
  const [_, fullTime] = ISODateTime.split("T");
  const [hour, minute] = fullTime.split(":");
  const { meridiem: meridiem, hour: meridiemHour } = getMeridiemTime(
    Number(hour)
  );

  return {
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

function useTimePicker({ remindDate }: Props) {
  const { hour, minute } = getDateInformation(new Date());
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
      const { meridiem, meridiemHour, minute } = parseTime(remindDate);
      changeMeridiem(meridiem);
      changeHour(meridiemHour);
      changeMinute(minute);
    }
  }, [remindDate]);

  useEffect(() => {
    if (meridiemRef.current) {
      meridiemRef.current.scrollTo({
        top: checkedMeridiem === "오전" ? 0 : PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
      });
    }

    if (AMHourRef.current) {
      AMHourRef.current.scrollTo({
        top: Number(checkedHour) * PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
      });
    }

    if (PMHourRef.current) {
      PMHourRef.current.scrollTo({
        top:
          (Number(checkedHour) === 12 ? 0 : Number(checkedHour)) *
          PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
      });
    }

    if (minuteRef.current) {
      minuteRef.current.scrollTo({
        top: (Number(checkedMinute) / 10) * PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
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

export default useTimePicker;
