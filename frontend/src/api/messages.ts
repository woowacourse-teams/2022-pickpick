import { ResponseMessages } from "@src/@types/shared";
import { fetcher } from ".";

export const getMessages = async ({ pageParam = 1 }) => {
  const { data } = await fetcher.get<ResponseMessages>(
    `/api/messages?page=${pageParam}&size=20`
  );

  return data;
};
