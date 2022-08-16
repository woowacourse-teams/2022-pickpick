import SearchOptions from "@src/components/SearchOptions";
import SearchInput from "@src/components/SearchInput";
import * as Styled from "./style";
import useSelectChannels from "@src/hooks/useSelectChannels";
import useSubmitSearchForm from "@src/hooks/useSubmitSearchForm";
import Dropdown from "../Dropdown";

interface Props {
  currentChannelIds: number[];
}

function SearchForm({ currentChannelIds }: Props) {
  const {
    allChannels,
    selectedChannelIds,
    handleToggleChannel,
    handleToggleAllChannels,
  } = useSelectChannels({ currentChannelIds });

  const {
    searchKeyword,
    handleChangeSearchKeyword,
    handleSubmitSearchKeyword,
  } = useSubmitSearchForm();

  return (
    <Dropdown>
      {({ isDropdownOpened, handleOpenDropdown }) => (
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
                currentChannels={allChannels.filter(({ id }) =>
                  currentChannelIds.includes(id)
                )}
                leftChannels={allChannels.filter(
                  ({ id }) => !currentChannelIds.includes(id)
                )}
                selectedChannelIds={selectedChannelIds}
                handleToggleChannel={handleToggleChannel}
                handleToggleAllChannels={handleToggleAllChannels}
              />
            )}
          </SearchInput>
        </Styled.Container>
      )}
    </Dropdown>
  );
}

export default SearchForm;
