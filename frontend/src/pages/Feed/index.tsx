import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import SearchInput from "@src/components/SearchInput";
import * as Styled from "./style";
import { useInfiniteQuery } from "react-query";
import { getMessages } from "@src/api/messages";
import { ResponseMessages } from "@src/@types/shared";
import React from "react";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import { extractResponseMessages } from "@src/@utils";
import useMessageDate from "@src/hooks/useMessageDate";
import { nextMessagesCallback } from "@src/api/utils";
import { QUERY_KEY } from "@src/@constants";
import useBookmark from "@src/hooks/useBookmark";
import { useLocation, useParams } from "react-router-dom";
import DateDropdown from "@src/components/DateDropdown";
import useModal from "@src/hooks/useModal";
import Portal from "@src/components/@shared/Portal";
import Dimmer from "@src/components/@shared/Dimmer";
import Calendar from "@src/components/Calendar";
import SearchOptions from "@src/components/SearchOptions";
import useChannelIds from "@src/hooks/useChannelIds";
import EmptyStatus from "@src/components/EmptyStatus";

function Feed() {
  const { channelId } = useParams();
  const { isRenderDate } = useMessageDate();
  const { key: queryKey } = useLocation();

  const { data, isLoading, isSuccess, fetchNextPage, hasNextPage, refetch } =
    useInfiniteQuery<ResponseMessages>(
      [QUERY_KEY.ALL_MESSAGES, queryKey],
      getMessages({
        channelId,
      }),
      {
        getNextPageParam: nextMessagesCallback,
      }
    );

  const {
    channelsData,
    channelIds,
    defaultChannel,
    handleToggleChannelId,
    handleToggleAllChannelIds,
  } = useChannelIds({ defaultChannelId: channelId ? Number(channelId) : 0 });

  const {
    isModalOpened: isSearchInputFocused,
    handleOpenModal: handleOpenSearchOptions,
    handleCloseModal: handleCloseSearchOptions,
  } = useModal();

  const {
    isModalOpened: isCalenderOpened,
    handleOpenModal: handleOpenCalendar,
    handleCloseModal: handleCloseCalendar,
  } = useModal();

  const { handleAddBookmark, handleRemoveBookmark } = useBookmark({
    handleSettle: refetch,
  });

  const parsedData = extractResponseMessages(data);

  return (
    <Styled.Container>
      {isSearchInputFocused && (
        <Dimmer hasBackgroundColor={false} onClick={handleCloseSearchOptions} />
      )}
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

      <InfiniteScroll
        callback={fetchNextPage}
        threshold={0.9}
        endPoint={!hasNextPage}
      >
        <FlexColumn gap="4px" width="100%">
          {isSuccess && parsedData.length === 0 && <EmptyStatus />}
          {parsedData.map(
            ({
              id,
              username,
              postedDate,
              text,
              userThumbnail,
              isBookmarked,
            }) => {
              const parsedDate = postedDate.split("T")[0];

              return (
                <React.Fragment key={id}>
                  {isRenderDate(parsedDate) && (
                    <DateDropdown
                      postedDate={parsedDate}
                      channelId={channelId ?? "main"}
                      handleOpenCalendar={handleOpenCalendar}
                    />
                  )}
                  <MessageCard
                    username={username}
                    date={postedDate}
                    text={text}
                    thumbnail={userThumbnail}
                    isBookmarked={isBookmarked}
                    toggleBookmark={
                      isBookmarked
                        ? handleRemoveBookmark(id)
                        : handleAddBookmark(id)
                    }
                  />
                </React.Fragment>
              );
            }
          )}

          {isLoading && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>

      <Portal isOpened={isCalenderOpened}>
        <>
          <Dimmer hasBackgroundColor={true} onClick={handleCloseCalendar} />
          <Calendar
            channelId={channelId ?? "main"}
            handleCloseCalendar={handleCloseCalendar}
          />
        </>
      </Portal>
    </Styled.Container>
  );
}

export default Feed;
