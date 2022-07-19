import { ResponseMessages } from "@src/@types/shared";
import { fetcher } from ".";

export const getMessages =
  ({ date = "" } = {}) =>
  async ({ pageParam }: any) => {
    console.log(pageParam);

    if (!pageParam) {
      const { data } = await fetcher.get<ResponseMessages>(
        `/api/messages?channelIds=5&messageId=&needPastMessage=${true}&date=${date}`
      );

      return data;
    }

    const {
      messageId = "",
      needPastMessage = true,
      date: currentDate = "",
    } = pageParam;

    const { data } = await fetcher.get<ResponseMessages>(
      `/api/messages?channelIds=5&messageId=${messageId}&needPastMessage=${needPastMessage}&date=${currentDate}`
    );

    return data;
  };
