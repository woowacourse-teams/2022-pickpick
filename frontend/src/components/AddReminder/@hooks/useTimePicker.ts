import {
  ChangeEventHandler,
  RefObject,
  useEffect,
  useRef,
  useState,
} from "react";

import useInput from "@src/hooks/@shared/useInput";

import { PICKER_OPTION_SCROLL } from "@src/@constants";
import { MERIDIEM, NOON } from "@src/@constants/date";
import { Meridiem } from "@src/@types/date";
import {
  getFullDateInformation,
  getMeridiemTimeFromISO,
  getTimeWithMeridiem,
  getTimeWithTenMinuteIntervals,
  isValidMeridiem,
} from "@src/@utils/date";

interface Props {
  remindDate: string;
}

interface UseTimePickerResult {
  meridiemRef: RefObject<HTMLDivElement>;
  AMHourRef: RefObject<HTMLDivElement>;
  PMHourRef: RefObject<HTMLDivElement>;
  minuteRef: RefObject<HTMLDivElement>;
  checkedMeridiem: Meridiem;
  checkedHour: number;
  checkedMinute: number;
  handleChangeMeridiem: ChangeEventHandler<HTMLInputElement>;
  handleChangeHour: ChangeEventHandler<HTMLInputElement>;
  handleChangeMinute: ChangeEventHandler<HTMLInputElement>;
  handleResetTimePickerPosition: () => void;
}

function useTimePicker({ remindDate }: Props): UseTimePickerResult {
  const { hour, minute } = getFullDateInformation(new Date());
  const { meridiem, hour: meridiemHour } = getTimeWithMeridiem(hour);

  const { parsedHour, parsedMinute } = getTimeWithTenMinuteIntervals({
    hour: meridiemHour,
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
  } = useInput<Meridiem>({
    initialValue: meridiem,
    validation: isValidMeridiem,
  });

  const {
    value: checkedHour,
    handleChangeValue: handleChangeHour,
    changeValue: changeHour,
  } = useInput<number>({
    initialValue: parsedHour,
  });

  const {
    value: checkedMinute,
    handleChangeValue: handleChangeMinute,
    changeValue: changeMinute,
  } = useInput<number>({ initialValue: parsedMinute });

  const handleResetTimePickerPosition = () => {
    setTimeDropdownFlag((prev) => !prev);
  };

  useEffect(() => {
    if (!remindDate) return;
    const { meridiem, meridiemHour, minute } =
      getMeridiemTimeFromISO(remindDate);
    changeMeridiem(meridiem);
    changeHour(meridiemHour);
    changeMinute(minute);
  }, [remindDate]);

  useEffect(() => {
    if (meridiemRef.current) {
      meridiemRef.current.scrollTo({
        top: checkedMeridiem === MERIDIEM.AM ? 0 : PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
      });
    }

    if (AMHourRef.current) {
      AMHourRef.current.scrollTo({
        top: checkedHour * PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
      });
    }

    if (PMHourRef.current) {
      PMHourRef.current.scrollTo({
        top:
          (checkedHour === NOON ? 0 : checkedHour) *
          PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
      });
    }

    if (minuteRef.current) {
      minuteRef.current.scrollTo({
        top: (checkedMinute / 10) * PICKER_OPTION_SCROLL.HEIGHT,
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
