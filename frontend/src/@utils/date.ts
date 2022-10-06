import {
  CONVERTER_SUFFIX,
  DATE,
  DAY,
  MERIDIEM,
  NOON,
  TIME_UNIT,
} from "@src/@constants/date";
import { Meridiem, MeridiemHours, StandardHours } from "@src/@types/date";

export type GetTimeWithMeridiem = (time: StandardHours) => {
  meridiem: Meridiem;
  hour: MeridiemHours;
};

// 일반 시간을 받아서 오전,오후 시간으로 바꾸어준다.
export const getTimeWithMeridiem: GetTimeWithMeridiem = (time) => {
  if (time < NOON)
    return { meridiem: MERIDIEM.AM, hour: time as MeridiemHours };

  if (time === NOON)
    return { meridiem: MERIDIEM.PM, hour: NOON as MeridiemHours };
  return {
    meridiem: MERIDIEM.PM,
    hour: (time - NOON) as MeridiemHours,
  };
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

type GetFullDateInformation = (givenDate: Date) => {
  year: number;
  month: number;
  date: number;
  day: string;
  hour: StandardHours;
  minute: number;
};

// date 객체를 받아서, 해당 date 객체의 전체 시간 정보를 반환한다.
export const getFullDateInformation: GetFullDateInformation = (givenDate) => {
  const year = givenDate.getFullYear();
  const month = givenDate.getMonth() + 1;
  const date = givenDate.getDate();
  const day = "월요일";
  const hour = givenDate.getHours() as StandardHours;
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

  return `${givenDate.month}${TIME_UNIT.MONTH} ${givenDate.date}${TIME_UNIT.DATE} ${givenDate.day}`;
};

type GetFutureDateOption = () => Record<"years" | "months" | "dates", number[]>;

// 오늘 기준으로 미래의 날짜 옵션 years, months, dates 를 반환한다.
export const getFutureDateOption: GetFutureDateOption = () => {
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

type GetMeridiemTimeFromISO = (ISODateTime: string) => {
  meridiem: Meridiem;
  meridiemHour: MeridiemHours;
  minute: number;
};

// ISO 시간을 받아서 MeridiemTime 을 반환한다.
export const getMeridiemTimeFromISO: GetMeridiemTimeFromISO = (ISODateTime) => {
  const [_, fullTime] = ISODateTime.split("T");
  const [hour, minute] = fullTime.split(":");
  const { meridiem: meridiem, hour: meridiemHour } = getTimeWithMeridiem(
    Number(hour) as StandardHours
  );

  return {
    meridiem,
    meridiemHour,
    minute: Number(minute),
  };
};

type GetDateFromISO = (
  ISODateTime: string
) => Record<"year" | "month" | "date", number>;

export const getDateFromISO: GetDateFromISO = (ISODateTime) => {
  const [fullDate] = ISODateTime.split("T");
  const [year, month, date] = fullDate.split("-");

  return {
    year: Number(year),
    month: Number(month),
    date: Number(date),
  };
};

type GetTimeWithTenMinuteIntervals = ({
  hour,
  minute,
}: {
  hour: number;
  minute: number;
}) => Record<"parsedHour" | "parsedMinute", number>;

// hour 와 minute (Meridiem 기준) 을 받아서 10분 단위 간격으로 반환해준다.
export const getTimeWithTenMinuteIntervals: GetTimeWithTenMinuteIntervals = ({
  hour,
  minute,
}) => {
  if (minute > 50) {
    return { parsedHour: hour + 1, parsedMinute: 0 };
  }

  return { parsedHour: hour, parsedMinute: Math.ceil(minute / 10) * 10 };
};

type GetFullHourFormMeridiemHour = (
  meridiemHour: number,
  meridiem: Meridiem
) => number;

export const getFullHourFromMeridiemHour: GetFullHourFormMeridiemHour = (
  meridiemHour,
  meridiem
) => {
  if (meridiem === MERIDIEM.PM) {
    return meridiemHour === NOON ? NOON : meridiemHour + NOON;
  }

  return meridiemHour;
};

type ParseMessageDateFromISO = (date: string) => string;

export const parseMessageDateFromISO: ParseMessageDateFromISO = (date) => {
  const { meridiem, meridiemHour, minute } = getMeridiemTimeFromISO(date);

  return `${meridiem} ${meridiemHour}:${minute.toString().padStart(2, "0")}`;
};

type IsValidMeridiem = (value: string) => boolean;

export const isValidMeridiem: IsValidMeridiem = (value) => {
  return value === MERIDIEM.AM || value === MERIDIEM.PM;
};

interface IsValidReminderTime {
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
}: IsValidReminderTime) => {
  if (checkedYear < year) return false;
  if (checkedYear === year && checkedMonth < month) return false;
  if (checkedYear === year && checkedMonth === month && checkedDate < date)
    return false;

  if (
    checkedYear === year &&
    checkedMonth === month &&
    checkedDate === date &&
    checkedHour < hour
  )
    return false;

  if (
    checkedYear === year &&
    checkedMonth === month &&
    checkedDate === date &&
    checkedHour === hour &&
    checkedMinute <= minute
  )
    return false;

  return true;
};
