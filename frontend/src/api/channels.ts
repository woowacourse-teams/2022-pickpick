import { ResponseChannels } from "@src/@types/shared";
import { fetcher } from ".";

export const getChannels = async () => {
  const { data } = await fetcher.get<ResponseChannels>("/api/channels", {
    headers: {
      authorization: "Bearer 47",
    },
  });
  return data;
};

export const subscribeChannel = async (id: string) => {
  await fetcher.post(
    "/api/channel-subscription",
    { id },
    {
      headers: {
        authorization: "Bearer 47",
      },
    }
  );
};

export const unsubscribeChannel = async (id: string) => {
  await fetcher.delete(`/api/channel-subscription/${id}`, {
    headers: {
      authorization: "Bearer 47",
    },
  });
};
