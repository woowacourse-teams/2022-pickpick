import { FlexColumn } from "@src/@styles/shared";
import Dropdown from "@src/components/Dropdown";
import MessageCard from "@src/components/MessageCard";
import SearchInput from "@src/components/SearchInput";
import * as Styled from "./style";
import { useEffect, useState } from "react";

function Home() {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    fetch("/api/messages")
      .then((response) => response.json())
      .then(({ messages }) => setMessages(messages));
  }, []);

  return (
    <Styled.Container>
      <SearchInput placeholder="검색 할 키워드를 입력해주세요." />
      <Styled.Wrapper>
        <Dropdown />
      </Styled.Wrapper>
      <FlexColumn gap="4px">
        {messages.map(({ id, username, postedDate, text, userThumbnail }) => (
          <MessageCard
            key={id}
            username={username}
            date={postedDate}
            text={text}
            thumbnail={userThumbnail}
          />
        ))}
      </FlexColumn>
    </Styled.Container>
  );
}

export default Home;
