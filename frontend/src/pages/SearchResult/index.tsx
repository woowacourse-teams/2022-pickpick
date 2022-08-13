import React from "react";
import * as Styled from "../Feed/style";
import { QUERY_KEY } from "@src/@constants";
import { ResponseMessages } from "@src/@types/shared";
import { getMessages } from "@src/api/messages";
import { nextMessagesCallback } from "@src/api/utils";
import { useInfiniteQuery } from "react-query";
import { useSearchParams } from "react-router-dom";
import MessageCard from "@src/components/MessageCard";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import { FlexColumn } from "@src/@styles/shared";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import EmptyStatus from "@src/components/EmptyStatus";
import useBookmark from "@src/hooks/useBookmark";
import { convertSeparatorToKey, extractResponseMessages } from "@src/@utils";

function SearchResult() {
  const [searchParams] = useSearchParams();
  const keyword = searchParams.get("keyword") ?? "";
  const channelIds = searchParams.get("channelIds") ?? "";

  const { data, isLoading, isSuccess, fetchNextPage, hasNextPage, refetch } =
    useInfiniteQuery<ResponseMessages>(
      QUERY_KEY.ALL_MESSAGES,
      getMessages({
        channelId: convertSeparatorToKey({
          key: "&channelIds=",
          separator: ",",
          value: channelIds,
        }),
        keyword,
      }),
      {
        getNextPageParam: nextMessagesCallback,
      }
    );

  const { handleAddBookmark, handleRemoveBookmark } = useBookmark({
    handleSettle: refetch,
  });

  const parsedData = extractResponseMessages(data);

  return (
    <Styled.Container>
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
            }) => (
              <React.Fragment key={id}>
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
            )
          )}

          {isLoading && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>
    </Styled.Container>
  );
}

export default SearchResult;
