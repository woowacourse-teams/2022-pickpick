import { QUERY_KEY } from "@src/@constants";
import { SubscribedChannel } from "@src/@types/shared";
import { getSubscribedChannels } from "@src/api/channels";
import { useEffect, useState } from "react";
import { useQuery } from "react-query";

interface Props {
  currentChannelIds: number[];
}

interface ReturnType {
  selectedChannelIds: number[];
  allChannels: SubscribedChannel[];
  handleToggleChannel: (id: number) => void;
  handleToggleAllChannels: () => void;
}

function useSelectChannels({ currentChannelIds }: Props): ReturnType {
  const [selectedChannelIds, setSelectedChannelIds] = useState<number[]>([]);
  const { data } = useQuery(
    QUERY_KEY.SUBSCRIBED_CHANNELS,
    getSubscribedChannels
  );
  const allChannels = data?.channels ?? [];

  const handleToggleAllChannels = () => {
    if (!allChannels) return;

    if (
      selectedChannelIds.length &&
      selectedChannelIds.length < allChannels.length
    ) {
      setSelectedChannelIds(allChannels.map((channel) => channel.id));
      return;
    }

    setSelectedChannelIds([]);
  };

  const handleToggleChannel = (id: number) => {
    if (selectedChannelIds.includes(id)) {
      const filteredChannelIds = selectedChannelIds.filter(
        (channelId) => id !== channelId
      );
      setSelectedChannelIds(filteredChannelIds);
      return;
    }

    setSelectedChannelIds((prev) => [...prev, id]);
  };

  useEffect(() => {
    if (allChannels.length === 0 || selectedChannelIds.length > 0) return;
    setSelectedChannelIds(
      currentChannelIds.length === 0 ? [allChannels[0].id] : currentChannelIds
    );
  }, [allChannels, currentChannelIds]);

  return {
    allChannels,
    selectedChannelIds,
    handleToggleChannel,
    handleToggleAllChannels,
  };
}

export default useSelectChannels;
