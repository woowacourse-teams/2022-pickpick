import Dimmer from "@src/components/@shared/Dimmer";
import SearchOptions from "@src/components/SearchOptions";
import SearchInput from "@src/components/SearchInput";
import * as Styled from "./style";
import useSelectChannels from "@src/hooks/useSelectChannels";
import useModal from "@src/hooks/useModal";
import { ChangeEvent, FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import { PATH_NAME } from "@src/@constants";
import useSnackbar from "@src/hooks/useSnackbar";

interface Props {
  channelId: number;
}

function SearchForm({ channelId }: Props) {
  const navigate = useNavigate();
  const [searchKeyword, setSearchKeyword] = useState("");

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

  const { openFailureSnackbar } = useSnackbar();

  const handleSubmit = (event: FormEvent) => {
    event.preventDefault();

    if (!channelIds.length) {
      openFailureSnackbar("채널을 하나 이상 선택 후 검색 버튼을 눌러주세요.");

      return;
    }

    if (!searchKeyword.length) {
      openFailureSnackbar(
        "검색할 키워드를 입력하신 후 검색 버튼을 눌러주세요."
      );

      return;
    }

    navigate(
      `${PATH_NAME.SEARCH_RESULT}?keyword=${searchKeyword}&channelIds=${channelIds}`
    );
  };

  const handleChangeSearchKeyword = (event: ChangeEvent<HTMLInputElement>) => {
    setSearchKeyword(event.target.value);
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
          onChange={handleChangeSearchKeyword}
          value={searchKeyword}
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
