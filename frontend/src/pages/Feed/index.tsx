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
import DateDropDown from "@src/components/DateDropdown";
import { nextMessagesCallback } from "@src/api/utils";
import { QUERY_KEY } from "@src/@constants";
import useBookmark from "@src/hooks/useBookmark";

function Feed() {
  const { initializeDateArray, isRenderDate } = useMessageDate();

  const { data, isLoading, isError, fetchNextPage, hasNextPage, refetch } =
    useInfiniteQuery<ResponseMessages>(QUERY_KEY.ALL_MESSAGES, getMessages(), {
      getNextPageParam: nextMessagesCallback,
      onSettled: initializeDateArray,
    });

  const { handleAddBookmark, handleRemoveBookmark } = useBookmark({
    handleSettle: refetch,
  });

  if (isError) return <div>이거슨 에러양!!!!</div>;

  return (
    <Styled.Container>
      <SearchInput placeholder="검색 할 키워드를 입력해주세요." />

      <InfiniteScroll
        callback={fetchNextPage}
        threshold={0.9}
        endPoint={!hasNextPage}
      >
        <FlexColumn gap="4px" width="100%">
          {extractResponseMessages(data).map(
            ({ id, username, postedDate, text, userThumbnail }) => {
              const parsedDate = postedDate.split("T")[0];

              return (
                <React.Fragment key={id}>
                  {isRenderDate(parsedDate) && (
                    <DateDropDown postedDate={parsedDate} />
                  )}
                  <MessageCard
                    id={id}
                    username={username}
                    date={postedDate}
                    text={text}
                    thumbnail={userThumbnail}
                    isBookmarked={false}
                    toggleBookmark={handleAddBookmark(id)}
                  />
                </React.Fragment>
              );
            }
          )}

          {isLoading && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>
    </Styled.Container>
  );
}

export default Feed;
