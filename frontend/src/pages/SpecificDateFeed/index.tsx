import * as Styled from "../Feed/style";
import React, { useEffect, useRef, useState } from "react";
import NextInfiniteScroll from "@src/components/@shared/NextInfiniteScroll";
import { useInfiniteQuery } from "react-query";
import { ResponseMessages } from "@src/@types/shared";
import { getMessages } from "@src/api/messages";
import MessageCardSkeleton from "@src/components/MessageCardSkeleton";
import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import Dropdown from "@src/components/Dropdown";
import { useLocation, useParams } from "react-router-dom";
import SearchInput from "@src/components/SearchInput";

function SpecificDateFeed() {
  const { key: queryKey } = useLocation();
  const { date } = useParams();
  const dateArrayRef = useRef<string[]>([]);
  const wheelPosition = useRef({ default: 0, move: 0, scroll: 0 });
  const touchPosition = useRef({ start: 0, end: 0 });
  const scrollPosition = useRef({ default: 0 });
  const flag = useRef(false);

  const {
    data,
    isFetching,
    isError,
    fetchPreviousPage,
    hasPreviousPage,
    fetchNextPage,
    hasNextPage,
  } = useInfiniteQuery<ResponseMessages>(
    ["dateMessages", queryKey],
    getMessages({
      date,
    }),
    {
      getPreviousPageParam: ({ isLast, messages }) => {
        if (!isLast) {
          return { messageId: messages[0]?.id, needPastMessage: false };
        }
      },
      getNextPageParam: ({ isLast, messages }) => {
        if (!isLast) {
          return {
            messageId: messages[messages.length - 1]?.id,
            needPastMessage: true,
          };
        }
      },
      onSettled: () => {
        dateArrayRef.current = [];
      },
    }
  );

  if (isError) return <div>이거슨 에러양!</div>;

  const renderDateDropdown = (postedDate: string) => {
    if (dateArrayRef.current.includes(postedDate)) return;

    dateArrayRef.current.push(postedDate);

    return (
      <Styled.Wrapper>
        <Dropdown postedDate={postedDate} />
      </Styled.Wrapper>
    );
  };

  const onWheel = (event: React.WheelEvent<HTMLDivElement>) => {
    wheelPosition.current.move = event.deltaY;

    if (
      wheelPosition.current.move < -10 &&
      !flag.current &&
      scrollPosition.current.default < 300
    ) {
      hasPreviousPage && fetchPreviousPage();
      flag.current = true;

      return;
    }

    if (wheelPosition.current.move > -10 && flag.current) {
      flag.current = false;

      return;
    }
  };

  const onTouchStart = (event: React.TouchEvent<HTMLDivElement>) => {
    touchPosition.current.start = event.changedTouches[0].clientY;
  };

  const onTouchEnd = (event: React.TouchEvent<HTMLDivElement>) => {
    touchPosition.current.end = event.changedTouches[0].clientY;

    const { start: touchStart, end: touchEnd } = touchPosition.current;

    if (touchStart - touchEnd < -50 && scrollPosition.current.default < 300) {
      hasPreviousPage && fetchPreviousPage();

      return;
    }
  };

  useEffect(() => {
    const handleScrollEvent = () => {
      scrollPosition.current.default = window.scrollY;
    };

    window.addEventListener("scroll", handleScrollEvent);

    return () => window.removeEventListener("scroll", handleScrollEvent);
  }, []);

  useEffect(() => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  }, [queryKey]);

  return (
    <Styled.Container
      onWheel={onWheel}
      onTouchStart={onTouchStart}
      onTouchEnd={onTouchEnd}
    >
      <SearchInput placeholder="검색 할 키워드를 입력해주세요." />
      <NextInfiniteScroll
        callback={fetchNextPage}
        threshold={0.9}
        endPoint={!hasNextPage}
      >
        <FlexColumn gap="4px" width="100%">
          {isFetching && (
            <>
              {Array.from({ length: 20 }).map((_, index) => (
                <MessageCardSkeleton key={index} />
              ))}
            </>
          )}

          {data?.pages
            .flatMap((arr) => arr.messages)
            .map(({ id, username, postedDate, text, userThumbnail }) => (
              <React.Fragment key={id}>
                {renderDateDropdown(postedDate.split("T")[0])}
                <MessageCard
                  username={username}
                  date={postedDate}
                  text={text}
                  thumbnail={userThumbnail}
                />
              </React.Fragment>
            ))}

          {isFetching && (
            <>
              {Array.from({ length: 20 }).map((_, index) => (
                <MessageCardSkeleton key={index} />
              ))}
            </>
          )}
        </FlexColumn>
      </NextInfiniteScroll>
    </Styled.Container>
  );
}

export default SpecificDateFeed;
