import { QUERY_KEY } from "@src/@constants";
import {
  ResponseSubscribedChannels,
  SubscribedChannel,
} from "@src/@types/shared";
import { getSubscribedChannels } from "@src/api/channels";
import { useEffect, useState } from "react";
import { useQuery } from "react-query";

interface Props {
  defaultChannelId?: number;
}

interface ReturnType {
  channelIds: (number | undefined)[];
  channelsData: ResponseSubscribedChannels | undefined;
  defaultChannel: SubscribedChannel | undefined;
  handleToggleChannelId: (id: number) => void;
  handleToggleAllChannelIds: () => void;
}

function useSelectChannels({ defaultChannelId }: Props): ReturnType {
  const [channelIds, setChannelIds] = useState<(number | undefined)[]>([]);

  const { data: channelsData } = useQuery(
    QUERY_KEY.SUBSCRIBED_CHANNELS,
    getSubscribedChannels
  );

  const defaultChannel = channelsData?.channels.filter((channel) => {
    if (defaultChannelId === 0) {
      return channel.id === channelsData?.channels[0].id;
    }

    return channel.id === defaultChannelId;
  })[0];

  const handleToggleAllChannelIds = () => {
    if (!channelsData) return;

    if (
      0 <= channelIds.length &&
      channelIds.length < channelsData.channels.length
    ) {
      setChannelIds(channelsData.channels.map((channel) => channel.id));

      return;
    }

    setChannelIds([]);
    return;
  };

  const handleToggleChannelId = (id: number) => {
    if (channelIds.includes(id)) {
      setChannelIds((prev) => {
        prev.splice(
          prev.findIndex((channelId) => channelId === id),
          1
        );

        return [...prev];
      });

      return;
    }

    setChannelIds((prev) => [...prev, id]);
    return;
  };

  useEffect(() => {
    setChannelIds([
      defaultChannelId === 0 ? channelsData?.channels[0].id : defaultChannelId,
    ]);
  }, [defaultChannelId]);

  return {
    channelIds,
    channelsData,
    defaultChannel,
    handleToggleChannelId,
    handleToggleAllChannelIds,
  };
}

export default useSelectChannels;
