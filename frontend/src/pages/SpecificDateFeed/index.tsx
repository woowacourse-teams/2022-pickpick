import * as Styled from "../Feed/style";
import React, { useEffect, useRef } from "react";
import NextInfiniteScroll from "@src/components/@shared/NextInfiniteScroll";
import { useInfiniteQuery } from "react-query";
import { ResponseMessages } from "@src/@types/shared";
import { getMessages } from "@src/api/messages";
import MessageCardSkeleton from "@src/components/MessageCardSkeleton";
import { FlexColumn } from "@src/@styles/shared";
import MessageCard from "@src/components/MessageCard";
import Dropdown from "@src/components/Dropdown";
import { useParams } from "react-router-dom";
import SearchInput from "@src/components/SearchInput";

function SpecificDateFeed() {
  const { date } = useParams();
  const dateArrayRef = useRef<string[]>([]);
  const wheelPosition = useRef({ default: 0, move: 0, scroll: 0 });
  const flag = useRef(false);
  const isInitialRender = useRef(true);

  const {
    data,
    isFetching,
    isError,
    fetchPreviousPage,
    hasPreviousPage,
    fetchNextPage,
    hasNextPage,
    refetch,
  } = useInfiniteQuery<ResponseMessages>(
    ["dateMessages"],
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
          return { messageId: messages.at(-1)?.id, needPastMessage: true };
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

  const onWheel = (event: any) => {
    wheelPosition.current.move = event.deltaY;

    if (
      wheelPosition.current.move < -10 &&
      !flag.current &&
      wheelPosition.current.scroll < 200
    ) {
      hasPreviousPage && fetchPreviousPage();
      flag.current = true;

      return;
    }

    if (wheelPosition.current.move > -30 && flag.current) {
      flag.current = false;

      return;
    }
  };

  useEffect(() => {
    function scrollEvent(event: any) {
      wheelPosition.current.scroll = event.scrollY;
    }

    window.addEventListener("scroll", scrollEvent);

    return window.removeEventListener("scroll", scrollEvent);
  }, []);

  useEffect(() => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  }, [date]);

  useEffect(() => {
    if (isInitialRender.current) {
      isInitialRender.current = false;

      return;
    }

    refetch();
  }, [date]);

  return (
    <Styled.Container onWheel={onWheel}>
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
                {renderDateDropdown(postedDate)}
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
