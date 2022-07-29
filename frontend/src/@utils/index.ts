import {
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
): Message[] => {
  if (!data) return [];

  return data.pages.flatMap((arr) => arr.bookmarks);
};
