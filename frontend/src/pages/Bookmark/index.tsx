import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import * as Styled from "../Feed/style";
import { useInfiniteQuery } from "react-query";
import { ResponseBookmarks, CustomError } from "@src/@types/shared";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import { extractResponseBookmarks, parseTime } from "@src/@utils";
import { nextBookmarksCallback } from "@src/api/utils";
import { QUERY_KEY } from "@src/@constants";
import { getBookmarks } from "@src/api/bookmarks";
import useBookmark from "@src/hooks/useBookmark";
import EmptyStatus from "@src/components/EmptyStatus";
import BookmarkButton from "@src/components/MessageIconButtons/BookmarkButton";
import { useEffect } from "react";

function Bookmark() {
  const { data, isLoading, isSuccess, fetchNextPage, hasNextPage, refetch } =
    useInfiniteQuery<ResponseBookmarks, CustomError>(
      QUERY_KEY.BOOKMARKS,
      getBookmarks,
      {
        getNextPageParam: nextBookmarksCallback,
      }
    );

  const { handleRemoveBookmark } = useBookmark({
    handleSettle: refetch,
  });

  const parsedData = extractResponseBookmarks(data);

  useEffect(() => {
    window.scrollTo({
      top: 0,
    });
  }, []);

  return (
    <Styled.Container>
      <InfiniteScroll
        callback={fetchNextPage}
        threshold={0.9}
        endPoint={!hasNextPage}
      >
        <FlexColumn gap="4px" width="100%">
          <>
            {isSuccess && parsedData.length === 0 && <EmptyStatus />}
            {parsedData.map(
              ({
                id,
                messageId,
                username,
                postedDate,
                text,
                userThumbnail,
              }) => (
                <MessageCard
                  key={id}
                  username={username}
                  isRemindedMessage={false}
                  date={parseTime(postedDate)}
                  text={text}
                  thumbnail={userThumbnail}
                >
                  <BookmarkButton
                    isActive={true}
                    onClick={handleRemoveBookmark(messageId)}
                  />
                </MessageCard>
              )
            )}
          </>
          {isLoading && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>
    </Styled.Container>
  );
}

export default Bookmark;
