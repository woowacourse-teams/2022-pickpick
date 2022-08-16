import React from "react";
import * as Styled from "../Feed/style";
import { QUERY_KEY } from "@src/@constants";
import { ResponseMessages } from "@src/@types/shared";
import { getMessages } from "@src/api/messages";
import { nextMessagesCallback } from "@src/api/utils";
import { useInfiniteQuery } from "react-query";
import MessageCard from "@src/components/MessageCard";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import { FlexColumn } from "@src/@styles/shared";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import useBookmark from "@src/hooks/useBookmark";
import { convertSeparatorToKey, extractResponseMessages } from "@src/@utils";
import SearchForm from "@src/components/SearchForm";
import useGetSearchParam from "@src/hooks/useGetSearchParam";

function SearchResult() {
  const keyword = useGetSearchParam("keyword");
  const channelIds = useGetSearchParam("channelIds");

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
      <SearchForm currentChannelIds={channelIds.split(",").map(Number)} />
      <InfiniteScroll
        callback={fetchNextPage}
        threshold={0.9}
        endPoint={!hasNextPage}
      >
        <FlexColumn gap="4px" width="100%">
          {isSuccess && parsedData.length === 0 && (
            <FlexColumn gap="30px" margin="25vh 0" alignItems="center">
              <h3>{`' ${keyword} '`} 에 대한 검색 결과가 없습니다.</h3>
            </FlexColumn>
          )}
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
