import { ResponseMessages } from "@src/@types/shared";
import { fetcher } from ".";

export const getMessages =
  ({ date = "", needPastMessage = true } = {}) =>
  async ({ pageParam = 0 }) => {
    const { data } = await fetcher.get<ResponseMessages>(
      `/api/messages?messageId=${pageParam}&size=20&needPastMessage=${needPastMessage}&date=${date}`
    );

    return data;
  };
