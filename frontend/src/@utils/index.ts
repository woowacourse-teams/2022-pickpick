import { CONVERTER_SUFFIX, DATE, DAY, TIME } from "@src/@constants/date";
import { Range } from "@src/@types/utils";

export type Meridiem = "오전" | "오후";

export type Hours = Range<0, 24>;

export type MeridiemHours = Range<1, 13>;

export type GetMeridiemTime = (time: Hours) => {
  meridiem: Meridiem;
  hour: MeridiemHours;
};

export const getMeridiemTime: GetMeridiemTime = (time) => {
  if (time < TIME.NOON)
    return { meridiem: TIME.AM, hour: time as MeridiemHours };

  if (time === TIME.NOON)
    return { meridiem: TIME.PM, hour: TIME.NOON as MeridiemHours };

  return {
    meridiem: TIME.PM,
    hour: (time - TIME.NOON) as MeridiemHours,
  };
};

type ParseMeridiemTime = (date: string) => string;

export const parseMeridiemTime: ParseMeridiemTime = (date) => {
  const dateInstance = new Date(date);
  const hour = dateInstance.getHours() as Range<0, 24>;
  const minute = dateInstance.getMinutes();
  const { meridiem, hour: parsedHour } = getMeridiemTime(hour);

  return `${meridiem} ${parsedHour}:${minute.toString().padStart(2, "0")}`;
};

type SetCookie = (key: string, value: string) => void;

export const setCookie: SetCookie = (key, value) => {
  document.cookie = `${key}=${value};`;
};

type GetCookie = (key: string) => string;

export const getCookie: GetCookie = (key) => {
  const regex = new RegExp(`${key}=([^;]+)`); // key(좌항)에 해당하는 우항을 가져온다. 세미콜론은 제외한다.
  const matches = document.cookie.match(regex);

  return matches ? matches[1] : "";
};

type DeleteCookie = (key: string) => void;

export const deleteCookie: DeleteCookie = (key) => {
  document.cookie = key + "=; Max-Age=0";
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

type GetDateInformation = (givenDate: Date) => {
  year: number;
  month: number;
  date: number;
  day: string;
  hour: number;
  minute: number;
};

export const getDateInformation: GetDateInformation = (givenDate) => {
  const year = givenDate.getFullYear();
  const month = givenDate.getMonth() + 1;
  const date = givenDate.getDate();
  const day = DAY[givenDate.getDay()];
  const hour = givenDate.getHours();
  const minute = givenDate.getMinutes();

  return { year, month, date, day, hour, minute };
};

type GetMessagesDate = (postedDate: string) => string;

export const getMessagesDate: GetMessagesDate = (postedDate) => {
  const givenDate = getDateInformation(new Date(postedDate));
  const today = getDateInformation(new Date());

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

type ParsedOptionText = ({
  needZeroPaddingStart,
  optionText,
}: {
  needZeroPaddingStart: boolean;
  optionText: string;
}) => string;

export const parsedOptionText: ParsedOptionText = ({
  needZeroPaddingStart,
  optionText,
}) => {
  return needZeroPaddingStart ? optionText.padStart(2, "0") : optionText;
};
