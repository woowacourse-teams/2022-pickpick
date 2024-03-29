import * as Styled from "@src/pages/Feed/style";

import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import EmptyStatus from "@src/components/EmptyStatus";
import MessageCard from "@src/components/MessageCard";
import BookmarkButton from "@src/components/MessageCard/MessageIconButtons/BookmarkButton";
import MessagesLoadingStatus from "@src/components/MessageCard/MessagesLoadingStatus";

import useGetInfiniteBookmarks from "@src/hooks/@query/useGetInfiniteBookmarks";
import useMutateBookmark from "@src/hooks/@query/useMutateBookmark";
import useScrollToTop from "@src/hooks/@shared/useScrollToTop";

import { FlexColumn, SrOnlyTitle } from "@src/@styles/shared";
import { extractResponseBookmarks } from "@src/@utils/api";
import { parseMessageDateFromISO } from "@src/@utils/date";

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
      <SrOnlyTitle>북마크</SrOnlyTitle>

      <InfiniteScroll
        callback={fetchNextPage}
        threshold={0.9}
        endPoint={!hasNextPage}
      >
        <FlexColumn gap="4px" width="100%" role="list">
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
              }) => {
                const date = postedDate.split("T")[0];

                return (
                  <MessageCard
                    key={id}
                    username={username}
                    isRemindedMessage={false}
                    date={`${date} ${parseMessageDateFromISO(postedDate)}`}
                    text={text}
                    thumbnail={userThumbnail}
                  >
                    <BookmarkButton
                      isActive={true}
                      onClick={handleRemoveBookmark(messageId)}
                    />
                  </MessageCard>
                );
              }
            )}
          </>

          {isLoading && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>
    </Styled.Container>
  );
}

export default Bookmark;
