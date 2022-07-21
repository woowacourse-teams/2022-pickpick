import { ResponseChannels } from "@src/@types/shared";
import { fetcher } from ".";

export const getChannels = async () => {
  const { data } = await fetcher.get<ResponseChannels>("/api/channels", {
    headers: {
      authorization: "Bearer 1",
    },
  });
  return data;
};
