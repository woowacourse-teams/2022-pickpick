import { fetcher } from "@src/api";

import { API_ENDPOINT, DEFAULT_CHANNEL_ID } from "@src/@constants/api";
import { GetMessagePageParam, ResponseMessages } from "@src/@types/api";
import { getPrivateHeaders } from "@src/@utils/api";

type GetMessages = ({
  date,
  channelId,
  keyword,
}: {
  date?: string;
  channelId?: string;
  keyword?: string;
}) => ({
  pageParam,
}: {
  pageParam?: GetMessagePageParam;
}) => Promise<ResponseMessages>;

export const getMessages: GetMessages =
  ({ date = "", channelId = "", keyword = "" }) =>
  async ({ pageParam }) => {
    if (!pageParam) {
      const { data } = await fetcher.get<ResponseMessages>(
        `${API_ENDPOINT.MESSAGES}?channelIds=${
          !channelId || channelId === DEFAULT_CHANNEL_ID ? "" : channelId
        }`,
        {
          headers: { ...getPrivateHeaders() },
          params: {
            keyword: keyword ?? "",
            messageId: "",
            needPastMessage: true,
            date: date ?? "",
          },
        }
      );

      return data;
    }

    const {
      messageId = "",
      needPastMessage = true,
      date: currentDate = "",
    } = pageParam;

    const { data } = await fetcher.get<ResponseMessages>(
      `${API_ENDPOINT.MESSAGES}?channelIds=${
        !channelId || channelId === DEFAULT_CHANNEL_ID ? "" : channelId
      }`,
      {
        headers: { ...getPrivateHeaders() },
        params: {
          keyword,
          messageId,
          needPastMessage,
          date: currentDate,
        },
      }
    );

    return data;
  };
