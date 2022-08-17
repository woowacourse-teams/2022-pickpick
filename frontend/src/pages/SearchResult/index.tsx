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
import {
  convertSeparatorToKey,
  extractResponseMessages,
  parseTime,
} from "@src/@utils";
import useModal from "@src/hooks/useModal";
import Portal from "@src/components/@shared/Portal";
import Dimmer from "@src/components/@shared/Dimmer";
import ReminderModal from "@src/components/ReminderModal";
import useSetTargetMessage from "@src/hooks/useSetTargetMessage";
import ReminderButton from "@src/components/MessageIconButtons/ReminderButton";
import BookmarkButton from "@src/components/MessageIconButtons/BookmarkButton";

function SearchResult() {
  const [searchParams] = useSearchParams();
  const keyword = searchParams.get("keyword") ?? "";
  const channelIds = searchParams.get("channelIds") ?? "";

  const {
    reminderTarget,
    handleUpdateReminderTarget,
    handleInitializeReminderTarget,
  } = useSetTargetMessage();

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

  const {
    isModalOpened: isReminderModalOpened,
    handleOpenModal: handleOpenReminderModal,
    handleCloseModal: handleCloseReminderModal,
  } = useModal();

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
                    onClick={handleOpenReminderModal}
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
            targetMessageId={reminderTarget.id}
            isTargetMessageSetReminded={reminderTarget.isSetReminded}
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
