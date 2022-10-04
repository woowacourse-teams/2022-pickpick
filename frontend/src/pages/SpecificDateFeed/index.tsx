import * as Styled from "@src/pages/Feed/style";
import { Fragment } from "react";
import { useLocation, useParams } from "react-router-dom";

import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import Modal from "@src/components/@shared/Modal";
import AddReminder from "@src/components/AddReminder";
import Calendar from "@src/components/Calendar";
import DateDropdown from "@src/components/DateDropdown";
import EmptyStatus from "@src/components/EmptyStatus";
import MessageCard from "@src/components/MessageCard";
import BookmarkButton from "@src/components/MessageCard/MessageIconButtons/BookmarkButton";
import ReminderButton from "@src/components/MessageCard/MessageIconButtons/ReminderButton";
import MessagesLoadingStatus from "@src/components/MessageCard/MessagesLoadingStatus";
import SearchForm from "@src/components/SearchForm";

import useGetInfiniteMessages from "@src/hooks/@query/useGetInfiniteMessages";
import useMutateBookmark from "@src/hooks/@query/useMutateBookmark";
import useModal from "@src/hooks/@shared/useModal";
import useScrollToTop from "@src/hooks/@shared/useScrollToTop";
import useMessageDate from "@src/hooks/useMessageDate";
import useSetReminderTargetMessage from "@src/hooks/useSetReminderTargetMessage";
import useTopScreenEventHandler from "@src/hooks/useTopScreenEventHandlers";

import { FlexColumn } from "@src/@styles/shared";
import { extractResponseMessages, parseMeridemTime } from "@src/@utils";

function SpecificDateFeed() {
  const { key: queryKey } = useLocation();
  const { date, channelId } = useParams();
  const shouldRenderDate = useMessageDate();

  const {
    reminderTarget,
    handleUpdateReminderTarget,
    handleInitializeReminderTarget,
  } = useSetReminderTargetMessage();

  const {
    data,
    isFetching,
    isSuccess,
    fetchPreviousPage,
    hasPreviousPage,
    fetchNextPage,
    hasNextPage,
    refetch,
  } = useGetInfiniteMessages({
    queryKey: [queryKey],
    channelId,
    date,
  });

  const {
    isModalOpened: isCalendarOpened,
    handleOpenModal: handleOpenCalendar,
    handleCloseModal: handleCloseCalendar,
  } = useModal();

  const {
    isModalOpened: isReminderModalOpened,
    handleOpenModal: handleOpenReminderModal,
    handleCloseModal: handleCloseReminderModal,
  } = useModal();

  const { onWheel, onTouchStart, onTouchEnd } = useTopScreenEventHandler({
    isCallable: hasPreviousPage,
    callback: fetchPreviousPage,
    scrollOffset: 300,
    touchDistanceCriterion: -50,
    wheelDistanceCriterion: -10,
  });

  const { handleAddBookmark, handleRemoveBookmark } = useMutateBookmark({
    handleSettleAddBookmark: refetch,
    handleSettleRemoveBookmark: refetch,
  });

  const parsedData = extractResponseMessages(data);

  useScrollToTop({ dependencies: [queryKey] });

  return (
    <Styled.Container
      onWheel={onWheel}
      onTouchStart={onTouchStart}
      onTouchEnd={onTouchEnd}
    >
      <SearchForm
        currentChannelIds={
          channelId && channelId !== "main" ? [Number(channelId)] : []
        }
      />

      <InfiniteScroll
        callback={fetchNextPage}
        threshold={0.9}
        endPoint={!hasNextPage}
      >
        <FlexColumn gap="4px" width="100%">
          {isFetching && <MessagesLoadingStatus length={20} />}
          {isSuccess && parsedData.length === 0 && <EmptyStatus />}
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
            }) => {
              const parsedDate = postedDate.split("T")[0];

              return (
                <Fragment key={id}>
                  {shouldRenderDate(parsedDate) && (
                    <DateDropdown
                      postedDate={parsedDate}
                      channelId={channelId ?? ""}
                      handleOpenCalendar={handleOpenCalendar}
                    />
                  )}
                  <MessageCard
                    username={username}
                    date={parseMeridemTime(postedDate)}
                    text={text}
                    thumbnail={userThumbnail}
                    isRemindedMessage={false}
                  >
                    <>
                      <ReminderButton
                        isActive={isSetReminded}
                        onClick={() => {
                          handleOpenReminderModal();
                          handleUpdateReminderTarget({ id, remindDate });
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
                </Fragment>
              );
            }
          )}

          {isFetching && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>

      <Modal isOpened={isCalendarOpened} handleCloseModal={handleCloseCalendar}>
        <Calendar
          channelId={channelId ?? "main"}
          handleCloseCalendar={handleCloseCalendar}
        />
      </Modal>

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

export default SpecificDateFeed;
