import { ResponseMessages } from "@src/@types/shared";

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
