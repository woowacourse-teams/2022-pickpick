import { InfiniteData } from "react-query";

import { ACCESS_TOKEN_KEY } from "@src/@constants/api";
import {
  Bookmark,
  Message,
  Reminder,
  ResponseBookmarks,
  ResponseMessages,
  ResponseReminders,
} from "@src/@types/api";
import { getCookie } from "@src/@utils";

export const getPublicHeaders = () => {
  return { "Access-Control-Allow-Origin": "*" };
};

export const getPrivateHeaders = () => {
  return {
    "Access-Control-Allow-Origin": "*",
    authorization: `Bearer ${getCookie(ACCESS_TOKEN_KEY)}`,
  };
};

type ExtractResponseMessages = (
  data?: InfiniteData<ResponseMessages>
) => Message[];

export const extractResponseMessages: ExtractResponseMessages = (data) => {
  if (!data) return [];

  return data.pages.flatMap((arr) => arr.messages);
};

type ExtractResponseBookmarks = (
  data?: InfiniteData<ResponseBookmarks>
) => Bookmark[];

export const extractResponseBookmarks: ExtractResponseBookmarks = (data) => {
  if (!data) return [];

  return data.pages.flatMap((arr) => arr.bookmarks);
};

type ExtractResponseReminders = (
  data?: InfiniteData<ResponseReminders>
) => Reminder[];

export const extractResponseReminders: ExtractResponseReminders = (data) => {
  if (!data) return [];

  return data.pages.flatMap((arr) => arr.reminders);
};

type GetChannelIdsParams = (channelIds: string) => string;

export const getChannelIdsParams: GetChannelIdsParams = (channelIds) => {
  const channelIdList = channelIds.split(",");
  if (channelIds.length === 1) return channelIdList[0];
  return channelIdList.join("&channelIds=");
};
