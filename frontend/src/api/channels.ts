import { API_ENDPOINT } from "@src/@constants";
import {
  ResponseChannels,
  ResponseSubscribedChannels,
} from "@src/@types/shared";
import { privateFetcher } from ".";

export const getChannels = async () => {
  const { data } = await privateFetcher.get<ResponseChannels>(
    API_ENDPOINT.CHANNEL
  );
  return data;
};

export const getSubscribedChannels = async () => {
  const { data } = await privateFetcher.get<ResponseSubscribedChannels>(
    API_ENDPOINT.CHANNEL_SUBSCRIPTION
  );

  return data;
};

export const subscribeChannel = async (channelId: string) => {
  await privateFetcher.post(API_ENDPOINT.CHANNEL_SUBSCRIPTION, { channelId });
};

export const unsubscribeChannel = async (channelId: string) => {
  await privateFetcher.delete(
    `${API_ENDPOINT.CHANNEL_SUBSCRIPTION}?channelId=${channelId}`
  );
};
