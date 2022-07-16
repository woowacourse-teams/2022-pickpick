import { ResponseMessages } from "@src/@types/shared";
import { fetcher } from ".";

export const getNextMessages = async ({ pageParam = 0 }) => {
  const { data } = await fetcher.get<ResponseMessages>(
    `/api/messages?messageId=${pageParam}&size=20&needPastMessage=true`
  );

  return data;
};

export const getPreviousMessages = async ({ pageParam = 0 }) => {
  const { data } = await fetcher.get<ResponseMessages>(
    `/api/messages?messageId=${pageParam}&size=20&needPastMessage=false`
  );

  return data;
};
