import { ResponseMessages } from "@src/@types/shared";
import { fetcher } from ".";

export const getMessages =
  ({ date = "" } = {}) =>
  async ({ pageParam }: any) => {
    if (!pageParam) {
      const { data } = await fetcher.get<ResponseMessages>(
        `/api/messages?messageId=${0}&size=20&needPastMessage=${true}&date=${date}`
      );

      return data;
    }

    const {
      messageId = 0,
      needPastMessage = true,
      date: currentDate = "",
    } = pageParam;

    const { data } = await fetcher.get<ResponseMessages>(
      `/api/messages?messageId=${messageId}&size=20&needPastMessage=${needPastMessage}&date=${currentDate}`
    );

    return data;
  };
