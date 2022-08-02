import {
  Bookmark,
  Message,
  ResponseBookmarks,
  ResponseMessages,
} from "@src/@types/shared";
import { InfiniteData } from "react-query";

const CONVERTER_SUFFIX = "T23:59:59";

const DATE = {
  TODAY: "오늘",
  YESTERDAY: "어제",
} as const;

const TIME = {
  AM: "오전",
  PM: "오후",
  NOON: 12,
} as const;

const getTimeStandard = (time: number): string => {
  if (time < TIME.NOON) return `${TIME.AM} ${time}`;
  if (time === TIME.NOON) return `${TIME.PM} ${TIME.NOON}`;

  return `${TIME.PM} ${time - TIME.NOON}`;
};

export const parseTime = (date: string): string => {
  const dateInstance = new Date(date);
  const hour = dateInstance.getHours();
  const minute = dateInstance.getMinutes();
  const timeStandard = getTimeStandard(Number(hour));

  return `${timeStandard}:${minute}`;
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

export const setCookie = (key: string, value: string) => {
  document.cookie = `${key}=${value};`;
};

export const getCookie = (key: string) => {
  const matches = document.cookie.match(
    new RegExp(
      "(?:^|; )" +
        // eslint-disable-next-line no-useless-escape
        key.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, "\\$1") +
        "=([^;]*)"
    )
  );
  return matches ? decodeURIComponent(matches[1]) : undefined;
};

export const deleteCookie = (key: string) => {
  setCookie(key, "");
};

export const ISOConverter = (date: string): string => {
  const today = new Date();

  if (date === DATE.TODAY) {
    return `${today.toISOString().split("T")[0]}${CONVERTER_SUFFIX}`;
  }

  if (date === DATE.YESTERDAY) {
    const yesterday = new Date(today.setDate(today.getDate() - 1));

    return `${yesterday.toISOString().split("T")[0]}${CONVERTER_SUFFIX}`;
  }

  const [year, month, day] = date.split("-");

  return `${year}-${month.padStart(2, "0")}-${day.padStart(
    2,
    "0"
  )}${CONVERTER_SUFFIX}`;
};
