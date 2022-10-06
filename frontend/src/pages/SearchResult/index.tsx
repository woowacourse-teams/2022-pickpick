import * as Styled from "@src/pages/Feed/style";

import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import Modal from "@src/components/@shared/Modal";
import AddReminder from "@src/components/AddReminder";
import MessageCard from "@src/components/MessageCard";
import BookmarkButton from "@src/components/MessageCard/MessageIconButtons/BookmarkButton";
import ReminderButton from "@src/components/MessageCard/MessageIconButtons/ReminderButton";
import MessagesLoadingStatus from "@src/components/MessageCard/MessagesLoadingStatus";
import SearchForm from "@src/components/SearchForm";

import useGetInfiniteMessages from "@src/hooks/@query/useGetInfiniteMessages";
import useMutateBookmark from "@src/hooks/@query/useMutateBookmark";
import useGetSearchParam from "@src/hooks/@shared/useGetSearchParam";
import useModal from "@src/hooks/@shared/useModal";
import useSetReminderTargetMessage from "@src/hooks/useSetReminderTargetMessage";

import { FlexColumn } from "@src/@styles/shared";
import { extractResponseMessages, getChannelIdsParams } from "@src/@utils/api";
import { parseMeridiemTime } from "@src/@utils/date";

function SearchResult() {
  const keyword = useGetSearchParam({ key: "keyword" });
  const channelIds = useGetSearchParam({ key: "channelIds" });

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
      channelId: getChannelIdsParams(channelIds),
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
                date={parseMeridiemTime(postedDate)}
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

      <Modal
        isOpened={isReminderModalOpened}
        handleCloseModal={() => {
          handleInitializeReminderTarget();
          handleCloseReminderModal();
        }}
      >
        <AddReminder
          messageId={reminderTarget.id}
          remindDate={reminderTarget.remindDate ?? ""}
          handleCloseReminderModal={() => {
            handleInitializeReminderTarget();
            handleCloseReminderModal();
          }}
          refetchFeed={refetch}
        />
      </Modal>
    </Styled.Container>
  );
}

export default SearchResult;
