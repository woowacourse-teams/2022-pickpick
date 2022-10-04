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

import useGetInfiniteMessages from "@src/hooks/query/useGetInfiniteMessages";
import useMutateBookmark from "@src/hooks/query/useMutateBookmark";
import useMessageDate from "@src/hooks/useMessageDate";
import useModal from "@src/hooks/useModal";
import useScrollToTop from "@src/hooks/useScrollToTop";
import useSetReminderTargetMessage from "@src/hooks/useSetReminderTargetMessage";

import { FlexColumn } from "@src/@styles/shared";
import { extractResponseMessages, parseMeridemTime } from "@src/@utils";

import * as Styled from "./style";

function Feed() {
  const { channelId } = useParams();
  const { isRenderDate } = useMessageDate();
  const { key: queryKey } = useLocation();

  const {
    reminderTarget,
    handleUpdateReminderTarget,
    handleInitializeReminderTarget,
  } = useSetReminderTargetMessage();

  const { data, isLoading, isSuccess, fetchNextPage, hasNextPage, refetch } =
    useGetInfiniteMessages({
      channelId,
      queryKey: [queryKey],
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

  const { handleAddBookmark, handleRemoveBookmark } = useMutateBookmark({
    handleSettleAddBookmark: refetch,
    handleSettleRemoveBookmark: refetch,
  });

  const parsedData = extractResponseMessages(data);

  useScrollToTop();

  return (
    <Styled.Container>
      <SearchForm currentChannelIds={channelId ? [Number(channelId)] : []} />

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
              remindDate,
              text,
              userThumbnail,
              isBookmarked,
              isSetReminded,
            }) => {
              const parsedDate = postedDate.split("T")[0];

              return (
                <Fragment key={id}>
                  {isRenderDate(parsedDate) && (
                    <DateDropdown
                      postedDate={parsedDate}
                      channelId={channelId ?? "main"}
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

          {isLoading && <MessagesLoadingStatus length={20} />}
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

export default Feed;
