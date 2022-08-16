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

function Reminder() {
  const { data, isLoading, isSuccess, fetchNextPage, hasNextPage } =
    useInfiniteQuery<ResponseReminders>(QUERY_KEY.REMINDERS, getReminders, {
      getNextPageParam: nextRemindersCallback,
    });

  const parsedData = extractResponseReminders(data);

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
              ({ id, username, postedDate, text, userThumbnail }) => (
                <MessageCard
                  key={id}
                  username={username}
                  date={postedDate}
                  text={text}
                  thumbnail={userThumbnail}
                  isBookmarked={true}
                  isSetReminded={true}
                />
              )
            )}
          </>
          {isLoading && <MessagesLoadingStatus length={20} />}
        </FlexColumn>
      </InfiniteScroll>
    </Styled.Container>
  );
}

export default Reminder;
