import {
  CONVERTER_SUFFIX,
  DATE,
  DAY,
  MERIDIEM,
  NOON,
  TIME,
} from "@src/@constants/date";
import { Range, ValueOf } from "@src/@types/utils";

export type Meridiem = ValueOf<typeof MERIDIEM>;

export type Hours = Range<0, 24>;

export type MeridiemHours = Range<1, 13>;

export type GetTimeWithMeridiem = (time: Hours) => {
  meridiem: Meridiem;
  hour: MeridiemHours;
};

// 일반 시간을 받아서 오전,오후 시간으로 바꾸어준다.
export const getTimeWithMeridiem: GetTimeWithMeridiem = (time) => {
  if (time < TIME.NOON)
    return { meridiem: MERIDIEM.AM, hour: time as MeridiemHours };

  if (time === TIME.NOON)
    return { meridiem: MERIDIEM.PM, hour: TIME.NOON as MeridiemHours };
  return {
    meridiem: TIME.PM,
    hour: (time - NOON) as MeridiemHours,
  };
};

type ParseMeridiemTime = (date: string) => string;

export const parseMeridiemTime: ParseMeridiemTime = (date) => {
  const dateInstance = new Date(date);
  const hour = dateInstance.getHours() as Range<0, 24>;
  const minute = dateInstance.getMinutes();
  const { meridiem, hour: parsedHour } = getTimeWithMeridiem(hour);

  return `${meridiem} ${parsedHour}:${minute.toString().padStart(2, "0")}`;
};

/**
 * TODO: 함수 분리
 */

export const ISOConverter = (date: string, time?: string): string => {
  const today = new Date();

  if (date === DATE.TODAY) {
    return `${today.toISOString().split("T")[0]}${CONVERTER_SUFFIX}`;
  }

  if (date === DATE.YESTERDAY) {
    const yesterday = new Date(today.setDate(today.getDate() - 1));

    return `${yesterday.toISOString().split("T")[0]}${CONVERTER_SUFFIX}`;
  }

  const [year, month, day] = date.split("-");

  if (time) {
    const [hour, minute] = time.split(":");

    return `${year}-${month.padStart(2, "0")}-${day.padStart(
      2,
      "0"
    )}${`T${hour.padStart(2, "0")}:${minute.padStart(2, "0")}:00`}`;
  }

  return `${year}-${month.padStart(2, "0")}-${day.padStart(
    2,
    "0"
  )}${CONVERTER_SUFFIX}`;
};

type getFullDateInformation = (givenDate: Date) => {
  year: number;
  month: number;
  date: number;
  day: string;
  hour: number;
  minute: number;
};

// date 객체를 받아서, 해당 date 객체의 전체 시간 정보를 반환한다.
export const getFullDateInformation: getFullDateInformation = (givenDate) => {
  const year = givenDate.getFullYear();
  const month = givenDate.getMonth() + 1;
  const date = givenDate.getDate();
  const day = "월요일";
  const hour = givenDate.getHours();
  const minute = givenDate.getMinutes();

  return { year, month, date, day, hour, minute };
};

type GetMessagesDate = (postedDate: string) => string;

export const getMessagesDate: GetMessagesDate = (postedDate) => {
  const givenDate = getFullDateInformation(new Date(postedDate));
  const today = getFullDateInformation(new Date());

  if (
    givenDate.year === today.year &&
    givenDate.month === today.month &&
    givenDate.date === today.date
  )
    return DATE.TODAY;

  if (
    givenDate.year === today.year &&
    givenDate.month === today.month &&
    givenDate.date === today.date - 1
  )
    return DATE.YESTERDAY;

  return `${givenDate.month}월 ${givenDate.date}일 ${givenDate.day}`;
};

type getTimeOption = () => Record<"AMHours" | "PMHours" | "minutes", number[]>;

// {
//   // meridiems: ["오전", "오후"];
//   // AMHours: ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"];
//   // PMHours: ["12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"];
//   // minutes: ["00", "10", "20", "30", "40", "50"];

// };

export const getTimeOption: getTimeOption = () => {
  const AMHours = Array.from({ length: 12 }, (_, index) => index);
  const PMHours = Array.from({ length: 12 }, (_, index) => {
    if (index === 0) return 12;

    return index;
  });
  const minutes = Array.from({ length: 6 }, (_, index) => index * 10);

  return {
    AMHours,
    PMHours,
    minutes,
  };
};

type getFutureDateOption = () => Record<"years" | "months" | "dates", number[]>;

// 오늘 기준으로 미래의 날짜 옵션 years, months, dates 를 반환한다.
export const getFutureDateOption = () => {
  const { year, month } = getFullDateInformation(new Date());
  const { date: lastDate } = getFullDateInformation(new Date(year, month, 0));

  const years = [year, year + 1, year + 2];
  const months = Array.from({ length: 12 }, (_, index) => index + 1);
  const dates = Array.from({ length: lastDate }, (_, index) => index + 1);

  return {
    years,
    months,
    dates,
  };
};

type getMeridiemTimeFormISO = (ISODateTime: string) => {
  meridiem: Meridiem;
  meridiemHour: MeridiemHours;
  minute: number;
};

// ISO 시간을 받아서 MeridiemTime 을 반환한다.
export const getMeridiemTimeFormISO: getMeridiemTimeFormISO = (ISODateTime) => {
  const [_, fullTime] = ISODateTime.split("T");
  const [hour, minute] = fullTime.split(":");
  const { meridiem: meridiem, hour: meridiemHour } = getTimeWithMeridiem(
    Number(hour) as Hours
  );

  return {
    meridiem,
    meridiemHour,
    minute: Number(minute),
  };
};

type getDateFromISO = (
  ISODateTime: string
) => Record<"year" | "month" | "date", number>;

export const getDateFromISO: getDateFromISO = (ISODateTime) => {
  const [fullDate] = ISODateTime.split("T");
  const [year, month, date] = fullDate.split("-");

  return {
    year: Number(year),
    month: Number(month),
    date: Number(date),
  };
};

type getTimeWithTenMinuteIntervals = ({
  hour,
  minute,
}: {
  hour: number;
  minute: number;
}) => Record<"parsedHour" | "parsedMinute", number>;

// hour 와 minute (Meridiem 기준) 을 받아서 10분 단위 간격으로 반환해준다.
export const getTimeWithTenMinuteIntervals: getTimeWithTenMinuteIntervals = ({
  hour,
  minute,
}) => {
  if (minute > 50) {
    return { parsedHour: hour + 1, parsedMinute: 0 };
  }

  return { parsedHour: hour, parsedMinute: Math.ceil(minute / 10) * 10 };
};

type isValidMeridiem = (value: string) => boolean;

export const isValidMeridiem: isValidMeridiem = (value) => {
  return value === MERIDIEM.AM || value === MERIDIEM.PM;
};

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

export const isValidReminderTime = ({
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
    checkedMinute <= minute
  )
    return true;

  return false;
};

export const getFullHourFromMeridiemHour = (
  meridiemHour: number,
  meridiem: Meridiem
): number => {
  if (meridiem === MERIDIEM.PM) {
    return meridiemHour === 12 ? 12 : meridiemHour + 12;
  }

  return meridiemHour;
};
