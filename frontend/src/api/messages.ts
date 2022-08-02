import { API_ENDPOINT } from "@src/@constants";
import { ResponseMessages } from "@src/@types/shared";
import { fetcher } from ".";
import { getHeaders } from "./utils";

interface PageParam {
  messageId: string;
  needPastMessage: boolean;
  date: string;
}

interface FirstParam {
  date?: string;
}

interface SecondParam {
  pageParam?: PageParam;
}

export const getMessages =
  ({ date }: FirstParam = { date: "" }) =>
  async ({ pageParam }: SecondParam) => {
    if (!pageParam) {
      const { data } = await fetcher.get<ResponseMessages>(
        `${
          API_ENDPOINT.MESSAGES
        }?channelIds=5&messageId=&needPastMessage=${true}&date=${date}`
      );

      return data;
    }

    const {
      messageId = "",
      needPastMessage = true,
      date: currentDate = "",
    } = pageParam;

    const { data } = await fetcher.get<ResponseMessages>(
      `${API_ENDPOINT.MESSAGES}?channelIds=5&messageId=${messageId}&needPastMessage=${needPastMessage}&date=${currentDate}`,
      {
        headers: getHeaders(),
      }
    );

    return data;
  };
