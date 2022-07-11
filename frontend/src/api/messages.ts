import { ResponseMessages } from "@src/@types/shared";
import { fetcher } from ".";

export const getMessages = async () => {
  const { data } = await fetcher.get<ResponseMessages>("/api/messages");

  return data;
};
