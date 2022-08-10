import SearchOptions from "@src/components/SearchOptions";
import SearchInput from "@src/components/SearchInput";
import * as Styled from "./style";
import useSelectChannels from "@src/hooks/useSelectChannels";
import useSearchKeywordForm from "@src/hooks/useSearchKeywordForm";
import Dropdown from "../Dropdown";

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
    searchKeyword,
    handleChangeSearchKeyword,
    handleSubmitSearchKeyword,
  } = useSearchKeywordForm({ channelIds });

  return (
    <Dropdown>
      {({ isDropdownOpened, handleToggleDropdown }) => (
        <Styled.Container onSubmit={handleSubmitSearchKeyword}>
          <SearchInput
            placeholder="검색 할 키워드를 입력해주세요."
            onFocus={handleToggleDropdown}
            onChange={handleChangeSearchKeyword}
            value={searchKeyword}
          >
            {channelsData && defaultChannel && isDropdownOpened && (
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
      )}
    </Dropdown>
  );
}

export default SearchForm;
