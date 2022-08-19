import * as Styled from "./style";
import Button from "@src/components/@shared/Button";
import { FlexColumn } from "@src/@styles/shared";
import { PATH_NAME } from "@src/@constants";
import { useMutation, useQuery } from "react-query";
import {
  getChannels,
  subscribeChannel,
  unsubscribeChannel,
} from "@src/api/channels";
import { QUERY_KEY } from "@src/@constants";
import { ResponseChannels, CustomError } from "@src/@types/shared";
import { Link } from "react-router-dom";

function AddChannel() {
  const { data, refetch } = useQuery<ResponseChannels, CustomError>(
    QUERY_KEY.ALL_CHANNELS,
    getChannels
  );

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
                isSubscribed ? unsubscribe(id) : subscribe(id);
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
