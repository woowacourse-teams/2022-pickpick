import Dimmer from "@src/components/@shared/Dimmer";
import SearchOptions from "@src/components/SearchOptions";
import SearchInput from "@src/components/SearchInput";
import * as Styled from "./style";
import useSelectChannels from "@src/hooks/useSelectChannels";
import useModal from "@src/hooks/useModal";
import { FormEvent } from "react";

interface Props {
  channelId: number;
}

function SearchForm({ channelId }: Props) {
  const {
    channelsData,
    channelIds,
    defaultChannel,
    handleToggleChannelId,
    handleToggleAllChannelIds,
  } = useSelectChannels({
    defaultChannelId: channelId ? Number(channelId) : 0,
  });

  const {
    isModalOpened: isSearchInputFocused,
    handleOpenModal: handleOpenSearchOptions,
    handleCloseModal: handleCloseSearchOptions,
  } = useModal();

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();
    console.log("submit");
  };

  return (
    <>
      {isSearchInputFocused && (
        <Dimmer hasBackgroundColor={false} onClick={handleCloseSearchOptions} />
      )}
      <Styled.Container onSubmit={handleSubmit}>
        <SearchInput
          placeholder="검색 할 키워드를 입력해주세요."
          onFocus={handleOpenSearchOptions}
        >
          {channelsData && defaultChannel && isSearchInputFocused && (
            <SearchOptions
              data={channelsData}
              defaultChannel={defaultChannel}
              channelIds={channelIds}
              handleToggleChannelId={handleToggleChannelId}
              handleToggleAllChannelIds={handleToggleAllChannelIds}
            />
          )}
        </SearchInput>
      </Styled.Container>
    </>
  );
}

export default SearchForm;
