import { ResponseChannels } from "@src/@types/shared";
import { fetcher } from ".";

export const getChannels = async () => {
  const { data } = await fetcher.get<ResponseChannels>("/api/channels", {
    headers: {
      authorization: "Bearer 857",
    },
  });
  return data;
};

export const subscribeChannel = async (channelId: string) => {
  await fetcher.post(
    "/api/channel-subscription",
    { channelId },
    {
      headers: {
        authorization: "Bearer 857",
      },
    }
  );
};

export const unsubscribeChannel = async (channelId: string) => {
  await fetcher.delete(`/api/channel-subscription?channelId=${channelId}`, {
    headers: {
      authorization: "Bearer 857",
    },
  });
};
