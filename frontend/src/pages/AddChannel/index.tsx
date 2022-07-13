import * as Styled from "./style";
import Button from "@src/components/@shared/Button";

const channels = [
  { id: 1, name: "4기-공지사항", isSubscribed: true },
  { id: 2, name: "4기-잡담", isSubscribed: true },
  { id: 3, name: "전체-잡담", isSubscribed: true },
  { id: 4, name: "전체-공지사항", isSubscribed: true },
  { id: 5, name: "학습블로그", isSubscribed: true },
  { id: 6, name: "4기-공지사항", isSubscribed: true },
  { id: 7, name: "be-4기-공지사항", isSubscribed: false },
  { id: 8, name: "be-4기-잡담", isSubscribed: false },
  { id: 9, name: "fe-4기-공지사항", isSubscribed: false },
  { id: 10, name: "fe-4기-잡담", isSubscribed: false },
];

function AddChannel() {
  return (
    <Styled.Container>
      <h1>채널 추가</h1>
      <p>추가하고 싶으신 채널을 선택해주세요</p>
      <Styled.ChannelListContainer>
        {channels.map(({ id, name, isSubscribed }) => (
          <Button key={id} size="medium" isActive={isSubscribed}>
            <>#{name}</>
          </Button>
        ))}
      </Styled.ChannelListContainer>
    </Styled.Container>
  );
}

export default AddChannel;
