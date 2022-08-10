import { API_ENDPOINT } from "@src/@constants";
import { ResponseMessages } from "@src/@types/shared";
import { fetcher } from ".";
import { getPrivateHeaders } from "./utils";

interface PageParam {
  messageId: string;
  needPastMessage: boolean;
  date: string;
}

interface HighOrderParam {
  date?: string;
  channelId?: string;
  keyword?: string;
}

interface ReturnFunctionParam {
  pageParam?: PageParam;
}

export const getMessages =
  ({ date = "", channelId = "", keyword = "" }: HighOrderParam) =>
  async ({ pageParam }: ReturnFunctionParam) => {
    if (!pageParam) {
      const { data } = await fetcher.get<ResponseMessages>(
        `${API_ENDPOINT.MESSAGES}?channelIds=${
          !channelId || channelId === "main" ? "" : channelId
        }&keyword=${keyword}&messageId=&needPastMessage=${true}&date=${
          date ?? ""
        }`,
        {
          headers: { ...getPrivateHeaders() },
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
        !channelId || channelId === "main" ? "" : channelId
      }&keyword=${keyword}&messageId=${messageId}&needPastMessage=${needPastMessage}&date=${currentDate}`,
      {
        headers: { ...getPrivateHeaders() },
      }
    );

    return data;
  };
