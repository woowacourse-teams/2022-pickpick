import { QUERY_KEY } from "@src/@constants";
import { getDateInformation, getMeridiemTime, ISOConverter } from "@src/@utils";
import {
  deleteReminder,
  getReminder,
  postReminder,
  putReminder,
} from "@src/api/reminders";
import { ChangeEvent, MouseEvent, useEffect, useRef, useState } from "react";
import { useMutation, useQuery } from "react-query";
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

const parsedDateTime = (ISODateTime: string) => {
  const [date, time] = ISODateTime.split("T");
  const [targetYear, targetMonth, targetDate] = date.split("-");
  const [targetHour, targetMinute, _] = time.split(":");
  const { meridiem: targetMeridiem, hour: targetMeridiemHour } =
    getMeridiemTime(Number(targetHour));

  return {
    targetYear,
    targetMonth,
    targetDate,
    targetMeridiem,
    targetMeridiemHour,
    targetMinute,
  };
};

interface HandleReminderSubmitProps {
  event: MouseEvent<HTMLButtonElement, globalThis.MouseEvent>;
  key: string;
}

interface Props {
  targetMessageId: string;
  isTargetMessageSetReminded: boolean;
  handleCloseReminderModal: () => void;
  refetchFeed: () => void;
}

function useSetReminder({
  targetMessageId,
  isTargetMessageSetReminded,
  handleCloseReminderModal,
  refetchFeed,
}: Props) {
  const { data: targetMessageData } = useQuery(
    QUERY_KEY.REMINDER,
    () => getReminder(targetMessageId),
    {
      enabled: isTargetMessageSetReminded,
    }
  );

  const { mutate: addReminder } = useMutation(postReminder, {
    onSuccess: () => {
      handleCloseReminderModal();
      refetchFeed();
    },
  });

  const { mutate: modifyReminder } = useMutation(putReminder, {
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

  const [checkedYear, setCheckedYear] = useState(`${year}년`);
  const [checkedMonth, setCheckedMonth] = useState(`${month}월`);
  const [checkedDate, setCheckedDate] = useState(`${date}일`);

  const [checkedMeridiem, setCheckedMeridiem] = useState(meridiem);
  const [checkedHour, setCheckedHour] = useState(`${parsedHour}시`);
  const [checkedMinute, setCheckedMinute] = useState(`${parsedMinute}분`);

  const years = [year, year + 1, year + 2].map((year) => `${year}년`);
  const months = Array.from({ length: 12 }, (_, index) => `${index + 1}월`);
  const dates = Array.from(
    { length: lastDate },
    (_, index) => `${index + 1}일`
  );

  const meridiems = ["오전", "오후"];
  const hours = Array.from({ length: 12 }, (_, index) => `${index + 1}시`);
  const minutes = Array.from({ length: 6 }, (_, index) => `${index * 10}분`);

  const replaceCheckedYear = Number(checkedYear.replace("년", ""));
  const replaceCheckedMonth = Number(checkedMonth.replace("월", ""));
  const replaceCheckedDate = Number(checkedDate.replace("일", ""));

  const replaceCheckedHour = convertMeridiemHourToStandardHour(
    checkedMeridiem,
    Number(checkedHour.replace("시", ""))
  );
  const replaceCheckedMinute = Number(checkedMinute.replace("분", ""));

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

  const handleReminderSubmit = ({ event, key }: HandleReminderSubmitProps) => {
    event.preventDefault();

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
        "리마인더 시간은 현재 시간보다 미래로 설정해주셔야 합니다."
      );

      return;
    }

    const reminderISODateTime = ISOConverter(
      `${replaceCheckedYear}-${replaceCheckedMonth}-${replaceCheckedDate}`,
      `${replaceCheckedHour}:${replaceCheckedMinute}`
    );

    if (key === "create") {
      addReminder({
        messageId: Number(targetMessageId),
        reminderDate: reminderISODateTime,
      });

      return;
    }

    modifyReminder({
      messageId: Number(targetMessageId),
      reminderDate: reminderISODateTime,
    });

    return;
  };

  const handleRemoveSubmit = async (targetMessageId: string) => {
    if (window.confirm("해당하는 메시지 리마인더를 정말 삭제하시겠습니까?")) {
      await deleteReminder(targetMessageId);

      refetchFeed();
      handleCloseReminderModal();

      return;
    }

    return;
  };

  useEffect(() => {
    if (isTargetMessageSetReminded && targetMessageData) {
      const {
        targetYear,
        targetMonth,
        targetDate,
        targetMeridiem,
        targetMeridiemHour,
        targetMinute,
      } = parsedDateTime(targetMessageData.remindDate);

      setCheckedYear(`${targetYear}년`);
      setCheckedMonth(`${targetMonth}월`);
      setCheckedDate(`${targetDate}일`);

      setCheckedMeridiem(targetMeridiem);
      setCheckedHour(`${targetMeridiemHour}시`);
      setCheckedMinute(`${targetMinute}분`);
    }
  }, [targetMessageData, isTargetMessageSetReminded]);

  useEffect(() => {
    if (yearRef.current) {
      yearRef.current.scrollTo({
        top: (replaceCheckedYear - year) * 22,
        behavior: "smooth",
      });
    }

    if (monthRef.current) {
      monthRef.current.scrollTo({
        top: (replaceCheckedMonth - 1) * 22.5,
        behavior: "smooth",
      });
    }

    if (dateRef.current) {
      dateRef.current.scrollTo({
        top: (replaceCheckedDate - 1) * 22.5,
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
        top: (replaceCheckedMinute / 10) * 22.7,
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
      handleReminderSubmit,
      handleRemoveSubmit,
    },
  };
}

export default useSetReminder;
