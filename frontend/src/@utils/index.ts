import {
  Bookmark,
  Message,
  ResponseBookmarks,
  ResponseMessages,
} from "@src/@types/shared";
import { InfiniteData } from "react-query";

const getTimeStandard = (time: number): string => {
  if (time < 12) return `오전 ${time}`;
  if (time === 12) return `오후 ${12}`;

  return `오후 ${time - 12}`;
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

export const ISOConverter = (date: string) => {
  const today = new Date();

  if (date === "오늘") {
    return today.toISOString().split("T")[0] + "T23:59:59";
  }

  if (date === "어제") {
    const yesterday = new Date(today.setDate(today.getDate() - 1));
    return yesterday.toISOString().split("T")[0] + "T23:59:59";
  }

  const [year, month, day] = date.split("-");
  return `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}T23:59:59`;
};
