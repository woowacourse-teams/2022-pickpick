import SearchOptions from ".";
import useSelectChannels from "../@hooks/useSelectChannels";

export default {
  title: "@component/SearchOptions",
  component: SearchOptions,
};

const Template = () => {
  const {
    allChannels,
    selectedChannelIds,
    getCurrentChannels,
    getRemainingChannels,
    handleToggleChannel,
    handleToggleAllChannels,
  } = useSelectChannels({ currentChannelIds: [] });

  return (
    <SearchOptions
      allChannels={allChannels}
      currentChannels={getCurrentChannels}
      remainingChannels={getRemainingChannels}
      selectedChannelIds={selectedChannelIds}
      handleToggleChannel={handleToggleChannel}
      handleToggleAllChannels={handleToggleAllChannels}
    />
  );
};

export const DefaultTemplate = Template.bind();
