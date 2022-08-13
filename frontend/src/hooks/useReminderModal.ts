import { getDateInformation, getMeridiemTime } from "@src/@utils";
import { ChangeEvent, useEffect, useRef, useState } from "react";

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
    if (meridiemHour === 12) {
      return 0;
    }

    return meridiemHour + 12;
  }

  return meridiemHour;
};

function useReminderModal() {
  const { year, month, date, hour, minute } = getDateInformation(new Date());
  const { meridiem, hour: meridiemHour } = getMeridiemTime(hour);
  const lastDate = new Date(year, month, 0).getDate();
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

  const [isDropdownOpened, setIsDropdownOpened] = useState(false);

  const [checkedMeridiem, setCheckedMeridiem] = useState(meridiem);
  const [checkedHour, setCheckedHour] = useState(`${parsedHour.toString()}시`);
  const [checkedMinute, setCheckedMinute] = useState(
    `${parsedMinute.toString()}분`
  );

  const [checkedYear, setCheckedYear] = useState(`${year.toString()}년`);
  const [checkedMonth, setCheckedMonth] = useState(`${month.toString()}월`);
  const [checkedDate, setCheckedDate] = useState(`${date.toString()}일`);

  const meridiems = ["오전", "오후"];
  const hours = Array.from(
    { length: 12 },
    (_, index) => `${(index + 1).toString()}시`
  );
  const minutes = Array.from(
    { length: 6 },
    (_, index) => `${(index * 10).toString()}분`
  );
  const years = [year, year + 1, year + 2].map(
    (year) => `${year.toString()}년`
  );
  const months = Array.from(
    { length: 12 },
    (_, index) => `${(index + 1).toString()}월`
  );
  const dates = Array.from(
    { length: lastDate },
    (_, index) => `${(index + 1).toString()}일`
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

  const handleOpenDropdown = (isOpened: boolean) => {
    setIsDropdownOpened(isOpened);
  };

  const handleSubmit = () => {
    if (
      Number(checkedYear) < year ||
      Number(checkedMonth) < month ||
      Number(checkedDate) < date ||
      convertMeridiemHourToStandardHour(checkedMeridiem, Number(checkedHour)) <
        hour ||
      Number(checkedMinute) < minute
    ) {
      console.log("리마인더 시간은 현재 시간 이후로 설정해주셔야 합니다.");
      return;
    }

    console.log("서브밋 성공");
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
    isDropdownOpened,
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
      handleOpenDropdown,
      handleSubmit,
    },
  };
}

export default useReminderModal;
