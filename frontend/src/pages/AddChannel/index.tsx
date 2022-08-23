import * as Styled from "./style";
import Button from "@src/components/@shared/Button";
import { FlexColumn } from "@src/@styles/shared";
import { PATH_NAME } from "@src/@constants";
import { Link } from "react-router-dom";
import useMutateChannels from "@src/hooks/query/useMutateChannels";
import useGetChannels from "@src/hooks/query/useGetChannels";

function AddChannel() {
  const { data, refetch } = useGetChannels();
  const { handleSubscribeChannel, handleUnSubscribeChannel } =
    useMutateChannels({
      handleSettleSubscribeChannel: refetch,
      handleSettleUnsubscribeChannel: refetch,
    });

  return (
    <Styled.Container>
      <Styled.Title>채널 추가</Styled.Title>

      <Styled.Description>
        추가하고 싶으신 채널을 선택해주세요
      </Styled.Description>

      <FlexColumn gap="50px" alignItems="end">
        <Styled.ChannelListContainer>
          {data?.channels.map(({ id, name, isSubscribed }) => (
            <Button
              key={id}
              size="medium"
              isActive={isSubscribed}
              onClick={() => {
                isSubscribed
                  ? handleUnSubscribeChannel(id)
                  : handleSubscribeChannel(id);
              }}
            >
              <>#{name}</>
            </Button>
          ))}
        </Styled.ChannelListContainer>

        <Link to={PATH_NAME.FEED}>
          <Styled.Button>다음</Styled.Button>
        </Link>
      </FlexColumn>
    </Styled.Container>
  );
}

export default AddChannel;
