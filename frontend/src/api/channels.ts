import { fetcher } from "@src/api";

import { API_ENDPOINT } from "@src/@constants/api";
import { ResponseChannels, ResponseSubscribedChannels } from "@src/@types/api";
import { getPrivateHeaders } from "@src/@utils/api";

type GetChannels = () => Promise<ResponseChannels>;

export const getChannels: GetChannels = async () => {
  const { data } = await fetcher.get<ResponseChannels>(API_ENDPOINT.CHANNEL, {
    headers: { ...getPrivateHeaders() },
  });
  return data;
};

type GetSubscribedChannels = () => Promise<ResponseSubscribedChannels>;

export const getSubscribedChannels: GetSubscribedChannels = async () => {
  const { data } = await fetcher.get<ResponseSubscribedChannels>(
    API_ENDPOINT.CHANNEL_SUBSCRIPTION,
    {
      headers: { ...getPrivateHeaders() },
    }
  );

  return data;
};

type SubscribeChannel = (channelId: string) => Promise<void>;

export const subscribeChannel: SubscribeChannel = async (channelId) => {
  await fetcher.post(
    API_ENDPOINT.CHANNEL_SUBSCRIPTION,
    { channelId },
    {
      headers: { ...getPrivateHeaders() },
    }
  );
};

type UnsubscribeChannel = (channelId: string) => Promise<void>;

export const unsubscribeChannel: UnsubscribeChannel = async (channelId) => {
  await fetcher.delete(API_ENDPOINT.CHANNEL_SUBSCRIPTION, {
    headers: { ...getPrivateHeaders() },
    params: {
      channelId,
    },
  });
};
