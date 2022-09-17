import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseMessages } from "@src/@types/shared";
import { getMessages } from "@src/api/messages";
import { useInfiniteQuery } from "react-query";

interface Props {
  queryKey: string[];
  channelId?: string;
  date?: string;
  keyword?: string;
}

function useGetInfiniteMessages({ channelId, date, keyword, queryKey }: Props) {
  return useInfiniteQuery<ResponseMessages, CustomError>(
    [QUERY_KEY.ALL_MESSAGES, ...queryKey],
    getMessages({
      channelId,
      date,
      keyword,
    }),
    {
      getPreviousPageParam: ({ hasFuture, messages }: ResponseMessages) => {
        if (hasFuture) {
          return { messageId: messages[0]?.id, needPastMessage: false };
        }
      },
      getNextPageParam: ({ hasPast, messages }: ResponseMessages) => {
        if (hasPast) {
          return {
            messageId: messages[messages.length - 1]?.id,
            needPastMessage: true,
          };
        }
      },
    }
  );
}

export default useGetInfiniteMessages;
