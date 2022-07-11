import { FlexColumn } from "@src/@styles/shared";
import Dropdown from "@src/components/Dropdown";
import MessageCard from "@src/components/MessageCard";
import SearchInput from "@src/components/SearchInput";
import MessageCardSkeleton from "@src/components/MessageCardSkeleton";
import * as Styled from "./style";
import { useQuery } from "react-query";
import { getMessages } from "@src/api/messages";

interface Message {
  id: string;
  username: string;
  postedDate: string;
  text: string;
  userThumbnail: string;
}

interface Response {
  messages: Message[];
}

function Home() {
  const { data, isLoading, isError } = useQuery<Response>(
    "messages",
    getMessages
  );

  if (isError) return <div>이거슨 에러양!!!!</div>;

  return (
    <Styled.Container>
      <SearchInput placeholder="검색 할 키워드를 입력해주세요." />
      <Styled.Wrapper>
        <Dropdown />
      </Styled.Wrapper>
      <FlexColumn gap="4px" width="100%">
        {data?.messages.map(
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
    </Styled.Container>
  );
}

export default Home;
