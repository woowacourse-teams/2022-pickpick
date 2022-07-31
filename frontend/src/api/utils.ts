import { ResponseMessages, ResponseBookmarks } from "@src/@types/shared";

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

export const getAuthorization = () => {
  return { authorization: "Bearer 2892" };
};
