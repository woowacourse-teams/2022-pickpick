import * as Styled from "@src/pages/Feed/style";

import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import Modal from "@src/components/@shared/Modal";
import AddReminder from "@src/components/AddReminder";
import EmptyStatus from "@src/components/EmptyStatus";
import MessageCard from "@src/components/MessageCard";
import ReminderButton from "@src/components/MessageCard/MessageIconButtons/ReminderButton";
import MessagesLoadingStatus from "@src/components/MessageCard/MessagesLoadingStatus";

import useGetInfiniteReminders from "@src/hooks/@query/useGetInfiniteReminders";
import useModal from "@src/hooks/@shared/useModal";
import useScrollToTop from "@src/hooks/@shared/useScrollToTop";
import useSetReminderTargetMessage from "@src/hooks/useSetReminderTargetMessage";

import { FlexColumn } from "@src/@styles/shared";
import { extractResponseReminders, parseMeridiemTime } from "@src/@utils";

function Reminder() {
  const {
    reminderTarget,
    handleUpdateReminderTarget,
    handleInitializeReminderTarget,
  } = useSetReminderTargetMessage();

  const { data, isLoading, isSuccess, fetchNextPage, hasNextPage, refetch } =
    useGetInfiniteReminders();

  const parsedData = extractResponseReminders(data);

  const {
    isModalOpened: isReminderModalOpened,
    handleOpenModal: handleOpenReminderModal,
    handleCloseModal: handleCloseReminderModal,
  } = useModal();

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
                remindDate,
                text,
                userThumbnail,
              }) => {
                const date = remindDate.split("T")[0];

                return (
                  <MessageCard
                    key={id}
                    username={username}
                    date={`${date} ${parseMeridiemTime(remindDate)}`}
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

export default Reminder;
