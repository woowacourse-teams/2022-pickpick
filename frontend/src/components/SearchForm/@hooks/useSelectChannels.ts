import { useCallback, useEffect, useMemo, useState } from "react";

import useGetSubscribedChannels from "@src/hooks/query/useGetSubscribedChannels";

import { SubscribedChannel } from "@src/@types/shared";

interface Props {
  currentChannelIds: number[];
}

interface ReturnType {
  allChannels: SubscribedChannel[];
  selectedChannelIds: number[];
  getCurrentChannels: SubscribedChannel[];
  getRemainingChannels: SubscribedChannel[];
  handleToggleChannel: (id: number) => void;
  handleToggleAllChannels: () => void;
}

function useSelectChannels({ currentChannelIds }: Props): ReturnType {
  const [visitingChannelIds, setVisitingChannelIds] =
    useState<number[]>(currentChannelIds);
  const [selectedChannelIds, setSelectedChannelIds] = useState<number[]>([]);
  const { data } = useGetSubscribedChannels();

  const allChannels = useMemo(() => data?.channels ?? [], [data?.channels]);

  const getCurrentChannels = useMemo(() => {
    return allChannels.filter(({ id }) => currentChannelIds.includes(id));
  }, [currentChannelIds]);

  const getRemainingChannels = useMemo(() => {
    return allChannels.filter(({ id }) => !currentChannelIds.includes(id));
  }, [currentChannelIds]);

  const handleToggleAllChannels = useCallback(() => {
    if (!allChannels) return;

    if (selectedChannelIds.length === allChannels.length) {
      setSelectedChannelIds([]);
      return;
    }

    setSelectedChannelIds(allChannels.map((channel) => channel.id));
  }, [selectedChannelIds]);

  const handleToggleChannel = useCallback(
    (id: number) => {
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
    },
    [selectedChannelIds]
  );

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
    getCurrentChannels,
    getRemainingChannels,
    handleToggleChannel,
    handleToggleAllChannels,
  };
}

export default useSelectChannels;
