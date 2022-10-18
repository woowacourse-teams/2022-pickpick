import { Link } from "react-router-dom";

import Button from "@src/components/@shared/Button";

import useGetChannels from "@src/hooks/@query/useGetChannels";
import useMutateChannels from "@src/hooks/@query/useMutateChannels";

import { PATH_NAME } from "@src/@constants/path";
import { FlexColumn } from "@src/@styles/shared";

import * as Styled from "./style";

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
        <Styled.ChannelListContainer role="list">
          {data?.channels.map(({ id, name, isSubscribed }) => (
            <Button
              key={id}
              size="medium"
              styleType={isSubscribed ? "primary" : "tertiary"}
              onClick={() => {
                isSubscribed
                  ? handleUnSubscribeChannel(id)
                  : handleSubscribeChannel(id);
              }}
              role="listItem"
              aria-label={`${name} 채널은 ${
                isSubscribed ? "구독중 입니다." : "구독중이 아닙니다."
              } ${name} 채널 ${isSubscribed ? "구독 해제" : "구독"}`}
            >
              <>#{name}</>
            </Button>
          ))}
        </Styled.ChannelListContainer>

        <Link to={PATH_NAME.FEED} role="button">
          <Styled.Button type="button">채널 선택 완료</Styled.Button>
        </Link>
      </FlexColumn>
    </Styled.Container>
  );
}

export default AddChannel;
