import { API_ENDPOINT } from "@src/@constants";
import {
  ResponseChannels,
  ResponseSubscribedChannels,
} from "@src/@types/shared";
import { fetcher } from ".";
import { getPrivateHeaders } from "./utils";

export const getChannels = async () => {
  const { data } = await fetcher.get<ResponseChannels>(API_ENDPOINT.CHANNEL, {
    headers: { ...getPrivateHeaders() },
  });
  return data;
};

export const getSubscribedChannels = async () => {
  const { data } = await fetcher.get<ResponseSubscribedChannels>(
    API_ENDPOINT.CHANNEL_SUBSCRIPTION,
    {
      headers: { ...getPrivateHeaders() },
    }
  );

  return data;
};

export const subscribeChannel = async (channelId: string) => {
  await fetcher.post(
    API_ENDPOINT.CHANNEL_SUBSCRIPTION,
    { channelId },
    {
      headers: { ...getPrivateHeaders() },
    }
  );
};

export const unsubscribeChannel = async (channelId: string) => {
  await fetcher.delete(
    `${API_ENDPOINT.CHANNEL_SUBSCRIPTION}?channelId=${channelId}`,
    {
      headers: { ...getPrivateHeaders() },
    }
  );
};
