import { FlexColumn } from "@src/@styles/shared";
import Dropdown from "@src/components/Dropdown";
import MessageCard from "@src/components/MessageCard";
import SearchInput from "@src/components/SearchInput";
import MessageCardSkeleton from "@src/components/MessageCardSkeleton";
import * as Styled from "./style";
import { InfiniteData, useInfiniteQuery } from "react-query";
import { getMessages } from "@src/api/messages";
import { Message, ResponseMessages } from "@src/@types/shared";
import InfiniteScroll from "@src/components/@shared/InfiniteScroll";

function Home() {
  const { data, isLoading, isError, fetchNextPage, hasNextPage } =
    useInfiniteQuery<ResponseMessages>(["messages"], getMessages, {
      getNextPageParam: ({ isLast, nextPage }) => {
        if (!isLast) {
          return nextPage;
        }
      },
    });

  if (isError) return <div>이거슨 에러양!!!!</div>;

  const extractMessages = (
    data?: InfiniteData<ResponseMessages>
  ): Message[] => {
    if (!data) return [];

    return data.pages.flatMap((arr) => arr.messages);
  };

  return (
    <Styled.Container>
      <SearchInput placeholder="검색 할 키워드를 입력해주세요." />
      <Styled.Wrapper>
        <Dropdown />
      </Styled.Wrapper>
      <InfiniteScroll
        callback={fetchNextPage}
        threshold={0.9}
        endPoint={!hasNextPage}
      >
        <FlexColumn gap="4px" width="100%">
          {extractMessages(data).map(
            ({ id, username, postedDate, text, userThumbnail }) => (
              <MessageCard
                key={id}
                username={username}
                date={postedDate}
                text={text}
                thumbnail={userThumbnail}
              />
            )
          )}

          {isLoading && (
            <>
              {Array.from({ length: 20 }).map((_, index) => (
                <MessageCardSkeleton key={index} />
              ))}
            </>
          )}
        </FlexColumn>
      </InfiniteScroll>
    </Styled.Container>
  );
}

export default Home;
