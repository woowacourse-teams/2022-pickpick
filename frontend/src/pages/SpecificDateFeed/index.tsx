import * as Styled from "../Feed/style";
import React, { useEffect } from "react";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import { useInfiniteQuery } from "react-query";
import { ResponseMessages } from "@src/@types/shared";
import { getMessages } from "@src/api/messages";
import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import { useLocation, useParams } from "react-router-dom";
import SearchInput from "@src/components/SearchInput";
import useTopScreenEventHandler from "@src/hooks/useTopScreenEventHandlers";
import { previousMessagesCallback, nextMessagesCallback } from "@src/api/utils";
import useMessageDate from "@src/hooks/useMessageDate";
import DateDropDown from "@src/components/DateDropdown";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import { extractResponseMessages } from "@src/@utils";

function SpecificDateFeed() {
  const { key: queryKey } = useLocation();
  const { date } = useParams();
  const { initializeDateArray, isRenderDate } = useMessageDate();
  const {
    data,
    isFetching,
    isError,
    fetchPreviousPage,
    hasPreviousPage,
    fetchNextPage,
    hasNextPage,
  } = useInfiniteQuery<ResponseMessages>(
    ["dateMessages", queryKey],
    getMessages({
      date,
    }),
    {
      getPreviousPageParam: previousMessagesCallback,
      getNextPageParam: nextMessagesCallback,
      onSettled: initializeDateArray,
    }
  );
  const { onWheel, onTouchStart, onTouchEnd } = useTopScreenEventHandler({
    isCallable: hasPreviousPage,
    callback: fetchPreviousPage,
    scrollOffset: 300,
    touchDistanceCriterion: -50,
    wheelDistanceCriterion: -10,
  });

  if (isError) return <div>이거슨 에러양!</div>;

  useEffect(() => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  }, [queryKey]);

  return (
    <Styled.Container
      onWheel={onWheel}
      onTouchStart={onTouchStart}
      onTouchEnd={onTouchEnd}
    >
      <SearchInput placeholder="검색 할 키워드를 입력해주세요." />

      <InfiniteScroll
        callback={fetchNextPage}
        threshold={0.9}
        endPoint={!hasNextPage}
      >
        <FlexColumn gap="4px" width="100%">
          {isFetching && <MessagesLoadingStatus length={20} />}

          {extractResponseMessages(data).map(
            ({ id, username, postedDate, text, userThumbnail }) => {
              const parsedDate = postedDate.split("T")[0];

              return (
                <React.Fragment key={id}>
                  {isRenderDate(parsedDate) && (
                    <DateDropDown postedDate={parsedDate} />
                  )}
                  <MessageCard
                    username={username}
                    date={postedDate}
                    text={text}
                    thumbnail={userThumbnail}
                  />
                </React.Fragment>
              );
            }
          )}

          {isFetching && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>
    </Styled.Container>
  );
}

export default SpecificDateFeed;
