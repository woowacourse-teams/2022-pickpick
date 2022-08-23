import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseMessages } from "@src/@types/shared";
import { getMessages } from "@src/api/messages";
import { useInfiniteQuery } from "react-query";
import { nextMessagesCallback, previousMessagesCallback } from "@src/api/utils";

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
      getPreviousPageParam: previousMessagesCallback,
      getNextPageParam: nextMessagesCallback,
    }
  );
}

export default useGetInfiniteMessages;
