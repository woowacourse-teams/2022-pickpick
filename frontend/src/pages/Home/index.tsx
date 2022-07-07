import MockImage from "@public/assets/images/MockImage.png";
import { FlexColumn } from "@src/@styles/shared";
import Dropdown from "@src/components/Dropdown";
import MessageCard from "@src/components/MessageCard";
import SearchInput from "@src/components/SearchInput";
import * as Styled from "./style";

const testArray = [
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
  {
    username: "포코(장현석)",
    date: "오후 2:23",
    text: `어쩌구저쩌
  어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
   어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
    thumbnail: MockImage,
  },
];

function Home() {
  return (
    <Styled.Container>
      <SearchInput placeholder="검색 할 키워드를 입력해주세요." />
      <Styled.Wrapper>
        <Dropdown />
      </Styled.Wrapper>
      <FlexColumn gap="4px">
        {testArray.map(({ username, date, text, thumbnail }, index) => (
          <MessageCard
            key={index}
            username={username}
            date={date}
            text={text}
            thumbnail={thumbnail}
          />
        ))}
      </FlexColumn>
    </Styled.Container>
  );
}

export default Home;
