import * as Styled from "./style";
import Button from "@src/components/@shared/Button";
import { FlexColumn } from "@src/@styles/shared";
import WrapperLink from "@src/components/@shared/WrapperLink";
import { PATH_NAME } from "@src/@constants";
import { useMutation, useQuery } from "react-query";
import {
  getChannels,
  subscribeChannel,
  unsubscribeChannel,
} from "@src/api/channels";

function AddChannel() {
  const { data, refetch } = useQuery("channels", getChannels);
  const { mutate: subscribe } = useMutation(subscribeChannel, {
    onSettled: () => {
      refetch();
    },
  });

  const { mutate: unsubscribe } = useMutation(unsubscribeChannel, {
    onSettled: () => {
      refetch();
    },
  });

  return (
    <Styled.Container>
      <h1>채널 추가</h1>
      <p>추가하고 싶으신 채널을 선택해주세요</p>
      <FlexColumn gap="50px" alignItems="end">
        <Styled.ChannelListContainer>
          {data?.channels.map(({ id, name, isSubscribed }) => (
            <Button
              key={id}
              size="medium"
              isActive={isSubscribed}
              onClick={() => {
                isSubscribed ? unsubscribe(id) : subscribe(id);
              }}
            >
              <>#{name}</>
            </Button>
          ))}
        </Styled.ChannelListContainer>
        <WrapperLink to={PATH_NAME.FEED}>다음</WrapperLink>
      </FlexColumn>
    </Styled.Container>
  );
}

export default AddChannel;
