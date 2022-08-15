import { getDateInformation, getMeridiemTime, ISOConverter } from "@src/@utils";
import { postReminder } from "@src/api/reminders";
import { ChangeEvent, useEffect, useRef, useState } from "react";
import { useMutation } from "react-query";
import useDropdown from "./useDropdown";
import useSnackbar from "./useSnackbar";

interface IsInvalidateDateTimeProps {
  checkedYear: number;
  checkedMonth: number;
  checkedDate: number;
  checkedHour: number;
  checkedMinute: number;
  year: number;
  month: number;
  date: number;
  hour: number;
  minute: number;
}

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

const convertMeridiemHourToStandardHour = (
  meridiem: string,
  meridiemHour: number
): number => {
  if (meridiem === "오후") {
    return meridiemHour === 12 ? 0 : meridiemHour + 12;
  }

  return meridiemHour;
};

const isInvalidateDateTime = ({
  checkedYear,
  checkedMonth,
  checkedDate,
  checkedHour,
  checkedMinute,
  year,
  month,
  date,
  hour,
  minute,
}: IsInvalidateDateTimeProps) => {
  if (checkedYear < year) return true;
  if (checkedYear <= year && checkedMonth < month) return true;
  if (checkedYear <= year && checkedMonth <= month && checkedDate < date)
    return true;

  if (
    checkedYear <= year &&
    checkedMonth <= month &&
    checkedDate <= date &&
    checkedHour < hour
  )
    return true;

  if (
    checkedYear <= year &&
    checkedMonth <= month &&
    checkedDate <= date &&
    checkedHour <= hour &&
    checkedMinute < minute
  )
    return true;

  return false;
};

interface Props {
  targetMessageId: string;
  handleCloseReminderModal: () => void;
  refetchFeed: () => void;
}

function useSetReminder({
  targetMessageId,
  handleCloseReminderModal,
  refetchFeed,
}: Props) {
  const { mutate: addReminder } = useMutation(postReminder, {
    onSuccess: () => {
      handleCloseReminderModal();
      refetchFeed();
    },
  });

  const { year, month, date, hour, minute } = getDateInformation(new Date());
  const { date: lastDate } = getDateInformation(new Date(year, month, 0));
  const { meridiem, hour: meridiemHour } = getMeridiemTime(hour);
  const { parsedHour, parsedMinute } = convertTimeToStepTenMinuteTime({
    hour: meridiemHour,
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
  const { openFailureSnackbar } = useSnackbar();

  const [checkedMeridiem, setCheckedMeridiem] = useState(meridiem);
  const [checkedHour, setCheckedHour] = useState(`${parsedHour}시`);
  const [checkedMinute, setCheckedMinute] = useState(`${parsedMinute}분`);

  const [checkedYear, setCheckedYear] = useState(`${year}년`);
  const [checkedMonth, setCheckedMonth] = useState(`${month}월`);
  const [checkedDate, setCheckedDate] = useState(`${date}일`);

  const meridiems = ["오전", "오후"];
  const hours = Array.from({ length: 12 }, (_, index) => `${index + 1}시`);
  const minutes = Array.from({ length: 6 }, (_, index) => `${index * 10}분`);
  const years = [year, year + 1, year + 2].map((year) => `${year}년`);
  const months = Array.from({ length: 12 }, (_, index) => `${index + 1}월`);
  const dates = Array.from(
    { length: lastDate },
    (_, index) => `${index + 1}일`
  );

  const handleChangeMeridiem = (event: ChangeEvent<HTMLInputElement>) => {
    if (event.target.value === "오전" || event.target.value === "오후") {
      setCheckedMeridiem(event.target.value);
    }
  };

  const handleChangeHour = (event: ChangeEvent<HTMLInputElement>) => {
    setCheckedHour(event.target.value);
  };

  const handleChangeMinute = (event: ChangeEvent<HTMLInputElement>) => {
    setCheckedMinute(event.target.value);
  };

  const handleChangeYear = (event: ChangeEvent<HTMLInputElement>) => {
    setCheckedYear(event.target.value);
  };

  const handleChangeMonth = (event: ChangeEvent<HTMLInputElement>) => {
    setCheckedMonth(event.target.value);
  };

  const handleChangeDate = (event: ChangeEvent<HTMLInputElement>) => {
    setCheckedDate(event.target.value);
  };

  const handleSubmit = () => {
    const replaceCheckedYear = Number(checkedYear.replace("년", ""));
    const replaceCheckedMonth = Number(checkedMonth.replace("월", ""));
    const replaceCheckedDate = Number(checkedDate.replace("일", ""));
    const replaceCheckedHour = convertMeridiemHourToStandardHour(
      checkedMeridiem,
      Number(checkedHour.replace("시", ""))
    );
    const replaceCheckedMinute = Number(checkedMinute.replace("분", ""));

    if (
      isInvalidateDateTime({
        checkedYear: replaceCheckedYear,
        checkedMonth: replaceCheckedMonth,
        checkedDate: replaceCheckedDate,
        checkedHour: replaceCheckedHour,
        checkedMinute: replaceCheckedMinute,
        year,
        month,
        date,
        hour,
        minute,
      })
    ) {
      openFailureSnackbar(
        "리마인더 시간은 현재 시간 이후로 설정해주셔야 합니다."
      );

      return;
    }

    const reminderISODateTime = ISOConverter(
      `${replaceCheckedYear}-${replaceCheckedMonth}-${replaceCheckedDate}`,
      `${replaceCheckedHour}:${replaceCheckedMinute}`
    );

    addReminder({
      messageId: Number(targetMessageId),
      reminderDate: reminderISODateTime,
    });

    return;
  };

  useEffect(() => {
    if (yearRef.current) {
      yearRef.current.scrollTo({
        top: (Number(checkedYear.replace("년", "")) - year) * 22,
        behavior: "smooth",
      });
    }

    if (monthRef.current) {
      monthRef.current.scrollTo({
        top: (Number(checkedMonth.replace("월", "")) - 1) * 22.5,
        behavior: "smooth",
      });
    }

    if (dateRef.current) {
      dateRef.current.scrollTo({
        top: (Number(checkedDate.replace("일", "")) - 1) * 22.5,
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
        top: (Number(checkedHour.replace("시", "")) - 1) * 22.5,
        behavior: "smooth",
      });
    }

    if (minuteRef.current) {
      minuteRef.current.scrollTo({
        top: (Number(checkedMinute.replace("분", "")) / 10) * 22.7,
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
      handleSubmit,
    },
  };
}

export default useSetReminder;
