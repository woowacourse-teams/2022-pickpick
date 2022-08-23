import * as Styled from "../Feed/style";
import MessageCard from "@src/components/MessageCard";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import { FlexColumn } from "@src/@styles/shared";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import useMutateBookmark from "@src/hooks/query/useMutateBookmark";
import {
  convertSeparatorToKey,
  extractResponseMessages,
  parseTime,
} from "@src/@utils";
import useModal from "@src/hooks/useModal";
import Portal from "@src/components/@shared/Portal";
import Dimmer from "@src/components/@shared/Dimmer";
import ReminderModal from "@src/components/ReminderModal";
import useSetReminderTargetMessage from "@src/hooks/useSetReminderTargetMessage";
import ReminderButton from "@src/components/MessageIconButtons/ReminderButton";
import BookmarkButton from "@src/components/MessageIconButtons/BookmarkButton";
import SearchForm from "@src/components/SearchForm";
import useGetSearchParam from "@src/hooks/useGetSearchParam";
import useGetInfiniteMessages from "@src/hooks/query/useGetInfiniteMessages";

function SearchResult() {
  const keyword = useGetSearchParam("keyword");
  const channelIds = useGetSearchParam("channelIds");

  const {
    reminderTarget,
    handleUpdateReminderTarget,
    handleInitializeReminderTarget,
  } = useSetReminderTargetMessage();

  const {
    isModalOpened: isReminderModalOpened,
    handleOpenModal: handleOpenReminderModal,
    handleCloseModal: handleCloseReminderModal,
  } = useModal();

  const { data, isLoading, isSuccess, fetchNextPage, hasNextPage, refetch } =
    useGetInfiniteMessages({
      channelId: convertSeparatorToKey({
        key: "&channelIds=",
        separator: ",",
        value: channelIds,
      }),
      queryKey: [keyword, channelIds],
      keyword,
    });

  const { handleAddBookmark, handleRemoveBookmark } = useMutateBookmark({
    handleSettleAddBookmark: refetch,
    handleSettleRemoveBookmark: refetch,
  });

  const parsedData = extractResponseMessages(data);

  return (
    <Styled.Container>
      <SearchForm
        currentKeyword={keyword}
        currentChannelIds={channelIds.split(",").map(Number)}
      />
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
              remindDate,
              text,
              userThumbnail,
              isBookmarked,
              isSetReminded,
            }) => (
              <MessageCard
                key={id}
                username={username}
                date={parseTime(postedDate)}
                text={text}
                thumbnail={userThumbnail}
                isRemindedMessage={false}
              >
                <>
                  <ReminderButton
                    isActive={isSetReminded}
                    onClick={() => {
                      handleUpdateReminderTarget({ id, remindDate });
                      handleOpenReminderModal();
                    }}
                  />
                  <BookmarkButton
                    isActive={isBookmarked}
                    onClick={
                      isBookmarked
                        ? handleRemoveBookmark(id)
                        : handleAddBookmark(id)
                    }
                  />
                </>
              </MessageCard>
            )
          )}

          {isLoading && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>

      <Portal isOpened={isReminderModalOpened}>
        <>
          <Dimmer
            hasBackgroundColor={true}
            onClick={() => {
              handleInitializeReminderTarget();
              handleCloseReminderModal();
            }}
          />
          <ReminderModal
            messageId={reminderTarget.id}
            remindDate={reminderTarget.remindDate ?? ""}
            handleCloseReminderModal={() => {
              handleInitializeReminderTarget();
              handleCloseReminderModal();
            }}
            refetchFeed={refetch}
          />
        </>
      </Portal>
    </Styled.Container>
  );
}

export default SearchResult;
