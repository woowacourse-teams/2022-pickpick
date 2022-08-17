import { ACCESS_TOKEN_KEY } from "@src/@constants";
import {
  ResponseMessages,
  ResponseBookmarks,
  ResponseReminders,
} from "@src/@types/shared";
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

export const previousMessagesCallback = ({
  isLast,
  messages,
}: ResponseMessages) => {
  if (!isLast) {
    return { messageId: messages[0]?.id, needPastMessage: false };
  }
};

export const nextMessagesCallback = ({
  isLast,
  messages,
}: ResponseMessages) => {
  if (!isLast) {
    return {
      messageId: messages[messages.length - 1]?.id,
      needPastMessage: true,
    };
  }
};

export const nextBookmarksCallback = ({
  isLast,
  bookmarks,
}: ResponseBookmarks) => {
  if (!isLast) {
    return bookmarks[bookmarks.length - 1]?.id;
  }
};

export const nextRemindersCallback = ({
  isLast,
  reminders,
}: ResponseReminders) => {
  if (!isLast) {
    return reminders[reminders.length - 1]?.id;
  }
};
