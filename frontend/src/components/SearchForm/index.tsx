import Dropdown from "@src/components/@shared/Dropdown";
import useSelectChannels from "@src/components/SearchForm/@hooks/useSelectChannels";
import useSubmitSearchForm from "@src/components/SearchForm/@hooks/useSubmitSearchForm";
import SearchInput from "@src/components/SearchForm/SearchInput";
import SearchOptions from "@src/components/SearchForm/SearchOptions";

import * as Styled from "./style";

interface Props {
  currentKeyword?: string;
  currentChannelIds: number[];
}

function SearchForm({ currentKeyword, currentChannelIds }: Props) {
  const {
    allChannels,
    selectedChannelIds,
    getCurrentChannels,
    getRemainingChannels,
    handleToggleChannel,
    handleToggleAllChannels,
  } = useSelectChannels({ currentChannelIds });

  const {
    searchKeyword,
    handleChangeSearchKeyword,
    handleSubmitSearchKeyword,
  } = useSubmitSearchForm({ keyword: currentKeyword ?? "" });

  return (
    <Dropdown>
      {({ innerRef, isDropdownOpened, handleOpenDropdown }) => (
        <div ref={innerRef}>
          <Styled.Container
            onSubmit={handleSubmitSearchKeyword(selectedChannelIds)}
          >
            <SearchInput
              placeholder="검색 할 키워드를 입력해주세요."
              onFocus={handleOpenDropdown}
              onChange={handleChangeSearchKeyword}
              value={searchKeyword}
            >
              {allChannels && isDropdownOpened && (
                <SearchOptions
                  allChannels={allChannels}
                  currentChannels={getCurrentChannels}
                  remainingChannels={getRemainingChannels}
                  selectedChannelIds={selectedChannelIds}
                  handleToggleChannel={handleToggleChannel}
                  handleToggleAllChannels={handleToggleAllChannels}
                />
              )}
            </SearchInput>
          </Styled.Container>
        </div>
      )}
    </Dropdown>
  );
}

export default SearchForm;
