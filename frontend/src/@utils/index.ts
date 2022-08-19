import { CONVERTER_SUFFIX, DATE, DAY, TIME } from "@src/@constants";
import {
  Bookmark,
  Message,
  Reminder,
  ResponseBookmarks,
  ResponseMessages,
  ResponseReminders,
} from "@src/@types/shared";
import { InfiniteData } from "react-query";

export const getMeridiemTime = (time: number) => {
  if (time < TIME.NOON) return { meridiem: TIME.AM, hour: time.toString() };
  if (time === TIME.NOON)
    return { meridiem: TIME.PM, hour: TIME.NOON.toString() };

  return { meridiem: TIME.PM, hour: (time - TIME.NOON).toString() };
};

export const parseTime = (date: string): string => {
  const dateInstance = new Date(date);
  const hour = dateInstance.getHours();
  const minute = dateInstance.getMinutes();
  const { meridiem, hour: parsedHour } = getMeridiemTime(Number(hour));

  return `${meridiem} ${parsedHour}:${minute.toString().padStart(2, "0")}`;
};

export const extractResponseMessages = (
  data?: InfiniteData<ResponseMessages>
): Message[] => {
  if (!data) return [];

  return data.pages.flatMap((arr) => arr.messages);
};

export const extractResponseBookmarks = (
  data?: InfiniteData<ResponseBookmarks>
): Bookmark[] => {
  if (!data) return [];

  return data.pages.flatMap((arr) => arr.bookmarks);
};

export const extractResponseReminders = (
  data?: InfiniteData<ResponseReminders>
): Reminder[] => {
  if (!data) return [];

  return data.pages.flatMap((arr) => arr.reminders);
};

export const setCookie = (key: string, value: string) => {
  document.cookie = `${key}=${value};`;
};

export const getCookie = (key: string) => {
  const regex = new RegExp(`(?<=${key}=)[^;]*`); // key(좌항)에 해당하는 우항을 가져온다. 세미콜론은 제외한다.
  const matches = document.cookie.match(regex);

  return matches ? matches[0] : "";
};

export const deleteCookie = (key: string) => {
  document.cookie = key + "=; Max-Age=0";
};

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

export const getDateInformation = (givenDate: Date) => {
  const year = givenDate.getFullYear();
  const month = givenDate.getMonth() + 1;
  const date = givenDate.getDate();
  const day = DAY[givenDate.getDay()];
  const hour = givenDate.getHours();
  const minute = givenDate.getMinutes();

  return { year, month, date, day, hour, minute };
};

export const getMessagesDate = (postedDate: string): string => {
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

export const convertSeparatorToKey = ({
  value,
  separator,
  key,
}: {
  value: string;
  separator: string;
  key: string;
}) => {
  if (value.search(separator) === -1) {
    return value;
  }
  // eslint-disable-next-line
  return value.replace(/\,/g, key);
};

export const parsedOptionText = ({
  needZeroPaddingStart,
  optionText,
}: {
  needZeroPaddingStart: boolean;
  optionText: string;
}): string => {
  return needZeroPaddingStart ? optionText.padStart(2, "0") : optionText;
};
