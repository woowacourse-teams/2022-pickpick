import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import * as Styled from "./style";
import { useInfiniteQuery } from "react-query";
import { getMessages } from "@src/api/messages";
import { ResponseMessages } from "@src/@types/shared";
import React, { useState } from "react";
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
import EmptyStatus from "@src/components/EmptyStatus";
import SearchForm from "@src/components/SearchForm";
import ReminderModal from "@src/components/ReminderModal";

function Feed() {
  const { channelId } = useParams();
  const { isRenderDate } = useMessageDate();
  const { key: queryKey } = useLocation();
  const [targetMessageId, setTargetMessageId] = useState("");

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
    isModalOpened: isCalendarOpened,
    handleOpenModal: handleOpenCalendar,
    handleCloseModal: handleCloseCalendar,
  } = useModal();

  const {
    isModalOpened: isReminderModalOpened,
    handleOpenModal: handleOpenReminderModal,
    handleCloseModal: handleCloseReminderModal,
  } = useModal();

  const handleUpdateTargetMessageId = (id: string) => {
    setTargetMessageId(id);
  };

  const { handleAddBookmark, handleRemoveBookmark } = useBookmark({
    handleSettle: refetch,
  });

  const parsedData = extractResponseMessages(data);

  return (
    <Styled.Container>
      <SearchForm channelId={channelId ? Number(channelId) : 0} />

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
              isSetReminded,
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
                    isSetReminded={isSetReminded}
                    toggleBookmark={
                      isBookmarked
                        ? handleRemoveBookmark(id)
                        : handleAddBookmark(id)
                    }
                    handleOpenReminderModal={() => {
                      handleOpenReminderModal();
                      handleUpdateTargetMessageId(id);
                    }}
                  />
                </React.Fragment>
              );
            }
          )}

          {isLoading && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>

      <Portal isOpened={isCalendarOpened}>
        <>
          <Dimmer hasBackgroundColor={true} onClick={handleCloseCalendar} />
          <Calendar
            channelId={channelId ?? "main"}
            handleCloseCalendar={handleCloseCalendar}
          />
        </>
      </Portal>

      <Portal isOpened={isReminderModalOpened}>
        <>
          <Dimmer
            hasBackgroundColor={true}
            onClick={handleCloseReminderModal}
          />
          <ReminderModal
            targetMessageId={targetMessageId}
            handleCloseReminderModal={handleCloseReminderModal}
            refetchFeed={refetch}
          />
        </>
      </Portal>
    </Styled.Container>
  );
}

export default Feed;
