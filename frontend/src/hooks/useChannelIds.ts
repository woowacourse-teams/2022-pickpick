import { QUERY_KEY } from "@src/@constants";
import { getSubscribedChannels } from "@src/api/channels";
import { useEffect, useState } from "react";
import { useQuery } from "react-query";
import useModal from "./useModal";

interface Props {
  defaultChannelId?: number;
}

function useChannelIds({ defaultChannelId }: Props) {
  const [channelIds, setChannelIds] = useState<(number | undefined)[]>([]);

  const { data: channelsData } = useQuery(
    QUERY_KEY.SUBSCRIBED_CHANNELS,
    getSubscribedChannels
  );

  const {
    isModalOpened: isSearchInputFocused,
    handleOpenModal: handleOpenSearchOptions,
    handleCloseModal: handleCloseSearchOptions,
  } = useModal();

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
    isSearchInputFocused,
    channelsData,
    channelIds,
    defaultChannel,
    handleOpenSearchOptions,
    handleCloseSearchOptions,
    handleToggleChannelId,
    handleToggleAllChannelIds,
  };
}

export default useChannelIds;
