import { SubscribedChannel } from "@src/@types/shared";
import { useEffect, useState } from "react";
import useGetSubscribedChannels from "@src/hooks/useGetSubscribedChannels";

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
  const { data } = useGetSubscribedChannels();
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
