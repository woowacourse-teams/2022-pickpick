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

import { DEFAULT_CHANNEL_ID } from "@src/@constants/api";
import { FlexColumn, SrOnlyTitle } from "@src/@styles/shared";
import { extractResponseMessages } from "@src/@utils/api";
import { parseMessageDateFromISO } from "@src/@utils/date";

import * as Styled from "./style";

function Feed() {
  const { channelId } = useParams();
  const { key: queryKey } = useLocation();
  const shouldRenderDate = useMessageDate();

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
      <SrOnlyTitle>메인 피드</SrOnlyTitle>

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
                  {shouldRenderDate(parsedDate) && (
                    <DateDropdown
                      postedDate={parsedDate}
                      channelId={channelId ?? DEFAULT_CHANNEL_ID}
                      handleOpenCalendar={handleOpenCalendar}
                    />
                  )}

                  <MessageCard
                    username={username}
                    date={parseMessageDateFromISO(postedDate)}
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
          channelId={channelId ?? DEFAULT_CHANNEL_ID}
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
