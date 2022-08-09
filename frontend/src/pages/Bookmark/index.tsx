import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import SearchInput from "@src/components/SearchInput";
import * as Styled from "../Feed/style";
import { useInfiniteQuery } from "react-query";
import { ResponseBookmarks } from "@src/@types/shared";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import { extractResponseBookmarks } from "@src/@utils";
import { nextBookmarksCallback } from "@src/api/utils";
import { QUERY_KEY } from "@src/@constants";
import { getBookmarks } from "@src/api/bookmarks";
import useBookmark from "@src/hooks/useBookmark";
import EmptyStatus from "@src/components/EmptyStatus";

function Bookmark() {
  const {
    data,
    isLoading,
    isSuccess,
    isError,
    fetchNextPage,
    hasNextPage,
    refetch,
  } = useInfiniteQuery<ResponseBookmarks>(QUERY_KEY.BOOKMARKS, getBookmarks, {
    getNextPageParam: nextBookmarksCallback,
  });

  const { handleRemoveBookmark } = useBookmark({
    handleSettle: refetch,
  });

  const parsedData = extractResponseBookmarks(data);

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
          <>
            {isSuccess && parsedData.length === 0 && <EmptyStatus />}
            {parsedData.map(
              ({ id, username, postedDate, text, userThumbnail }) => (
                <MessageCard
                  key={id}
                  username={username}
                  date={postedDate}
                  text={text}
                  thumbnail={userThumbnail}
                  isBookmarked={true}
                  toggleBookmark={handleRemoveBookmark(id)}
                />
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
