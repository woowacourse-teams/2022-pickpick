import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import * as Styled from "../Feed/style";
import { useInfiniteQuery } from "react-query";
import { ResponseReminders } from "@src/@types/shared";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";
import MessagesLoadingStatus from "@src/components/MessagesLoadingStatus";
import { extractResponseReminders } from "@src/@utils";
import { nextRemindersCallback } from "@src/api/utils";
import { QUERY_KEY } from "@src/@constants";
import EmptyStatus from "@src/components/EmptyStatus";
import { getReminders } from "@src/api/reminders";
import { useLocation } from "react-router-dom";
import Portal from "@src/components/@shared/Portal";
import Dimmer from "@src/components/@shared/Dimmer";
import ReminderModal from "@src/components/ReminderModal";
import useModal from "@src/hooks/useModal";
import useSetTargetMessage from "@src/hooks/useSetTargetMessage";

function Reminder() {
  const { pathname } = useLocation();
  const {
    messageTargetState: { targetMessageId, isTargetMessageSetReminded },
    handler: {
      handleUpdateTargetMessageId,
      handleUpdateTargetMessageSetReminded,
      handleInitializeTargetMessageId,
      handleInitializeTargetMessageSetReminded,
    },
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
                postedDate,
                text,
                userThumbnail,
              }) => (
                <MessageCard
                  key={id}
                  username={username}
                  pathname={pathname}
                  date={postedDate}
                  text={text}
                  thumbnail={userThumbnail}
                  isBookmarked={true}
                  isSetReminded={true}
                  handleOpenReminderModal={() => {
                    handleOpenReminderModal();
                    handleUpdateTargetMessageId(messageId.toString());
                    handleUpdateTargetMessageSetReminded(true);
                  }}
                />
              )
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
              handleInitializeTargetMessageId();
              handleInitializeTargetMessageSetReminded();
              handleCloseReminderModal();
            }}
          />
          <ReminderModal
            targetMessageId={targetMessageId}
            isTargetMessageSetReminded={isTargetMessageSetReminded}
            handleCloseReminderModal={() => {
              handleInitializeTargetMessageId();
              handleInitializeTargetMessageSetReminded();
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
