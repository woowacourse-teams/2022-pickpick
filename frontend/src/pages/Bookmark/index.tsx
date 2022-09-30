import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import * as Styled from "@src/pages/Feed/style";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import useMutateBookmark from "@src/hooks/query/useMutateBookmark";
import EmptyStatus from "@src/components/EmptyStatus";
import BookmarkButton from "@src/components/MessageIconButtons/BookmarkButton";
import { extractResponseBookmarks, parseTime } from "@src/@utils";
import useGetInfiniteBookmarks from "@src/hooks/query/useGetInfiniteBookmarks";
import useScrollToTop from "@src/hooks/useScrollToTop";

function Bookmark() {
  const { data, isLoading, isSuccess, fetchNextPage, hasNextPage, refetch } =
    useGetInfiniteBookmarks();

  const { handleRemoveBookmark } = useMutateBookmark({
    handleSettleRemoveBookmark: refetch,
  });

  const parsedData = extractResponseBookmarks(data);

  useScrollToTop();

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
