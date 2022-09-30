import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import * as Styled from "@src/pages/Feed/style";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import MessagesLoadingStatus from "@src/components/MessageCard/MessagesLoadingStatus";
import { extractResponseReminders, parseMeridemTime } from "@src/@utils";
import EmptyStatus from "@src/components/EmptyStatus";
import Portal from "@src/components/@shared/Portal";
import Dimmer from "@src/components/@shared/Dimmer";
import ReminderModal from "@src/components/ReminderModal";
import useModal from "@src/hooks/useModal";
import useSetReminderTargetMessage from "@src/hooks/useSetReminderTargetMessage";
import ReminderButton from "@src/components/MessageCard/MessageIconButtons/ReminderButton";
import useGetInfiniteReminders from "@src/hooks/query/useGetInfiniteReminders";
import useScrollToTop from "@src/hooks/useScrollToTop";

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
                    date={`${date} ${parseMeridemTime(remindDate)}`}
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
