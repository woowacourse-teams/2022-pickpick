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
  const [visitingChannelIds, setVisitingChannelIds] =
    useState<number[]>(currentChannelIds);
  const [selectedChannelIds, setSelectedChannelIds] = useState<number[]>([]);
  const { data } = useGetSubscribedChannels();
  const allChannels = data?.channels ?? [];

  const handleToggleAllChannels = () => {
    if (!allChannels) return;

    if (selectedChannelIds.length === allChannels.length) {
      setSelectedChannelIds([]);
      return;
    }
    setSelectedChannelIds(allChannels.map((channel) => channel.id));
  };

  const handleToggleChannel = (id: number) => {
    if (selectedChannelIds.includes(id)) {
      setSelectedChannelIds((prevChannelIds) => {
        const filteredChannelIds = prevChannelIds.filter(
          (channelId) => id !== channelId
        );

        return filteredChannelIds;
      });
      return;
    }

    setSelectedChannelIds((prev) => [...prev, id]);
  };

  useEffect(() => {
    if (allChannels.length === 0) return;
    setSelectedChannelIds(
      visitingChannelIds.length === 0 ? [allChannels[0].id] : visitingChannelIds
    );
  }, [allChannels, visitingChannelIds]);

  useEffect(() => {
    if (
      JSON.stringify(currentChannelIds) === JSON.stringify(visitingChannelIds)
    )
      return;
    setVisitingChannelIds(currentChannelIds);
  }, [currentChannelIds]);

  return {
    allChannels,
    selectedChannelIds,
    handleToggleChannel,
    handleToggleAllChannels,
  };
}

export default useSelectChannels;
