import * as Styled from "../Feed/style";
import React, { useRef } from "react";
import PreviousInfiniteScroll from "@src/components/@shared/PreviousInfiniteScroll";
import NextInfiniteScroll from "@src/components/@shared/NextInfiniteScroll";
import { InfiniteData, useInfiniteQuery } from "react-query";
import { Message, ResponseMessages } from "@src/@types/shared";
import { getMessages } from "@src/api/messages";
import MessageCardSkeleton from "@src/components/MessageCardSkeleton";
import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import Dropdown from "@src/components/Dropdown";
import { useParams } from "react-router-dom";

function SpecificDateFeed() {
  const { date } = useParams();
  const dateArrayRef = useRef<string[]>([]);

  const {
    data: previousData,
    isLoading: isPreviousDataLoading,
    isError: isPreviousDataError,
    fetchPreviousPage,
    hasPreviousPage,
  } = useInfiniteQuery<ResponseMessages>(
    ["datePreviousMessages"],
    getMessages({
      needPastMessage: false,
      date,
    }),
    {
      getPreviousPageParam: ({ isLast, messages }) => {
        if (!isLast) {
          return messages[0]?.id;
        }
      },
      onSettled: () => {
        dateArrayRef.current = [];
      },
    }
  );

  const {
    data: nextData,
    isLoading: isNextDataLoading,
    isError: isNextDataError,
    fetchNextPage,
    hasNextPage,
  } = useInfiniteQuery<ResponseMessages>(
    ["dateNextMessages"],
    getMessages({
      needPastMessage: true,
      date,
    }),
    {
      getNextPageParam: ({ isLast, messages }) => {
        if (!isLast) {
          return messages.at(-1)?.id;
        }
      },
      onSettled: () => {
        dateArrayRef.current = [];
      },
    }
  );

  if (isPreviousDataError || isNextDataError) return <div>이거슨 에러양!</div>;

  const extractMessages = (
    previousData?: InfiniteData<ResponseMessages>,
    nextData?: InfiniteData<ResponseMessages>
  ): Message[] => {
    if (!previousData || !nextData) return [];

    return [
      ...previousData.pages.flatMap((arr) => arr.messages),
      ...nextData.pages.flatMap((arr) => arr.messages),
    ];
  };

  const renderDateDropdown = (postedDate: string) => {
    if (dateArrayRef.current.includes(postedDate)) return;

    dateArrayRef.current.push(postedDate);

    return (
      <Styled.Wrapper>
        <Dropdown postedDate={postedDate} />
      </Styled.Wrapper>
    );
  };

  return (
    <Styled.Container>
      <PreviousInfiniteScroll
        callback={fetchPreviousPage}
        threshold={0.9}
        endPoint={!hasPreviousPage}
      >
        <NextInfiniteScroll
          callback={fetchNextPage}
          threshold={0.9}
          endPoint={!hasNextPage}
        >
          <FlexColumn gap="4px" width="100%">
            {isPreviousDataLoading && (
              <>
                {Array.from({ length: 20 }).map((_, index) => (
                  <MessageCardSkeleton key={index} />
                ))}
              </>
            )}

            {extractMessages(previousData, nextData).map(
              ({ id, username, postedDate, text, userThumbnail }) => (
                <React.Fragment key={id}>
                  <>
                    {renderDateDropdown(postedDate)}
                    <MessageCard
                      username={username}
                      date={postedDate}
                      text={text}
                      thumbnail={userThumbnail}
                    />
                  </>
                </React.Fragment>
              )
            )}

            {isNextDataLoading && (
              <>
                {Array.from({ length: 20 }).map((_, index) => (
                  <MessageCardSkeleton key={index} />
                ))}
              </>
            )}
          </FlexColumn>
        </NextInfiniteScroll>
      </PreviousInfiniteScroll>
    </Styled.Container>
  );
}

export default SpecificDateFeed;
