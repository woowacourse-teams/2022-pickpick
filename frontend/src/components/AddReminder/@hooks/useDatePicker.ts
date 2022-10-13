import {
  ChangeEventHandler,
  RefObject,
  useEffect,
  useRef,
  useState,
} from "react";

import useInput from "@src/hooks/@shared/useInput";

import { PICKER_OPTION_SCROLL } from "@src/@constants";
import { getDateFromISO, getFullDateInformation } from "@src/@utils/date";

interface Props {
  remindDate: string;
}

interface UseDatePickerResult {
  yearRef: RefObject<HTMLDivElement>;
  monthRef: RefObject<HTMLDivElement>;
  dateRef: RefObject<HTMLDivElement>;
  checkedYear: number;
  checkedMonth: number;
  checkedDate: number;
  handleChangeYear: ChangeEventHandler<HTMLInputElement>;
  handleChangeMonth: ChangeEventHandler<HTMLInputElement>;
  handleChangeDate: ChangeEventHandler<HTMLInputElement>;
  handleResetDatePickerPosition: () => void;
}

function useDatePicker({ remindDate }: Props): UseDatePickerResult {
  const { year, month, date } = getFullDateInformation(new Date());
  const yearRef = useRef<HTMLDivElement>(null);
  const monthRef = useRef<HTMLDivElement>(null);
  const dateRef = useRef<HTMLDivElement>(null);

  const [dateDropdownFlag, setDateDropdownFlag] = useState(false);

  const {
    value: checkedYear,
    handleChangeValue: handleChangeYear,
    changeValue: changeYear,
  } = useInput<number>({
    initialValue: year,
  });

  const {
    value: checkedMonth,
    handleChangeValue: handleChangeMonth,
    changeValue: changeMonth,
  } = useInput<number>({ initialValue: month });

  const {
    value: checkedDate,
    handleChangeValue: handleChangeDate,
    changeValue: changeDate,
  } = useInput<number>({
    initialValue: date,
  });

  const handleResetDatePickerPosition = () => {
    setDateDropdownFlag((prev) => !prev);
  };

  useEffect(() => {
    if (yearRef.current) {
      yearRef.current.scrollTo({
        top: (checkedYear - year) * PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
      });
    }

    if (monthRef.current) {
      monthRef.current.scrollTo({
        top: (checkedMonth - 1) * PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
      });
    }

    if (dateRef.current) {
      dateRef.current.scrollTo({
        top: (checkedDate - 1) * PICKER_OPTION_SCROLL.HEIGHT,
        behavior: PICKER_OPTION_SCROLL.BEHAVIOR,
      });
    }
  }, [checkedYear, checkedMonth, checkedDate, dateDropdownFlag]);

  useEffect(() => {
    if (remindDate) {
      const { year, month, date } = getDateFromISO(remindDate);

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
