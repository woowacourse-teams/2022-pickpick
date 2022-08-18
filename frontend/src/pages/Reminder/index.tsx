import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import * as Styled from "../Feed/style";
import { useInfiniteQuery } from "react-query";
import { ResponseReminders } from "@src/@types/shared";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import { extractResponseReminders, parseTime } from "@src/@utils";
import { nextRemindersCallback } from "@src/api/utils";
import { QUERY_KEY } from "@src/@constants";
import EmptyStatus from "@src/components/EmptyStatus";
import { getReminders } from "@src/api/reminders";
import Portal from "@src/components/@shared/Portal";
import Dimmer from "@src/components/@shared/Dimmer";
import ReminderModal from "@src/components/ReminderModal";
import useModal from "@src/hooks/useModal";
import useSetTargetMessage from "@src/hooks/useSetTargetMessage";
import ReminderButton from "@src/components/MessageIconButtons/ReminderButton";
import { useEffect } from "react";

function Reminder() {
  const {
    reminderTarget,
    handleUpdateReminderTarget,
    handleInitializeReminderTarget,
  } = useSetTargetMessage();

  const { data, isLoading, isSuccess, fetchNextPage, hasNextPage, refetch } =
    useInfiniteQuery<ResponseReminders>(QUERY_KEY.REMINDERS, getReminders, {
      getNextPageParam: nextRemindersCallback,
    });

  const parsedData = extractResponseReminders(data);

  const {
    isModalOpened: isReminderModalOpened,
    handleOpenModal: handleOpenReminderModal,
    handleCloseModal: handleCloseReminderModal,
  } = useModal();

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
                remindDate,
                text,
                userThumbnail,
              }) => {
                const date = remindDate.split("T")[0];

                return (
                  <MessageCard
                    key={id}
                    username={username}
                    date={`${date} ${parseTime(remindDate)}`}
                    text={text}
                    thumbnail={userThumbnail}
                    isRemindedMessage={true}
                  >
                    <ReminderButton
                      isActive={true}
                      onClick={() => {
                        handleOpenReminderModal();
                        handleUpdateReminderTarget({
                          id: messageId,
                          remindDate,
                        });
                      }}
                    />
                  </MessageCard>
                );
              }
            )}
          </>
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

export default Reminder;
