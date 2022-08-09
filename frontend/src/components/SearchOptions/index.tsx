import { FlexRow } from "@src/@styles/shared";
import {
  ResponseSubscribedChannels,
  SubscribedChannel,
} from "@src/@types/shared";
import Button from "../@shared/Button";
import * as Styled from "./style";

interface Props {
  data: ResponseSubscribedChannels;
  defaultChannel: SubscribedChannel;
  channelIds: (number | undefined)[];
  handleToggleChannelId: (id: number) => void;
  handleToggleAllChannelIds: () => void;
}

function SearchOptions({
  data,
  defaultChannel,
  channelIds,
  handleToggleChannelId,
  handleToggleAllChannelIds,
}: Props) {
  return (
    <Styled.Container>
      <p>검색에 포함할 채널을 선택해주세요.</p>

      <FlexRow gap="5px" overflow="auto">
        <Button
          isActive={channelIds.length === data.channels.length}
          size="medium"
          onClick={handleToggleAllChannelIds}
        >
          {channelIds.length === data.channels.length
            ? "전체 해제"
            : "전체 선택"}
        </Button>

        <Button
          key={defaultChannel.name}
          isActive={channelIds.includes(defaultChannel.id)}
          size="medium"
          onClick={() => handleToggleChannelId(defaultChannel.id)}
        >
          <>#{defaultChannel.name}</>
        </Button>

        {data.channels
          .filter((channel) => channel.id !== defaultChannel.id)
          .map((channel) => (
            <Button
              key={channel.name}
              isActive={channelIds.includes(channel.id)}
              size="medium"
              onClick={() => handleToggleChannelId(channel.id)}
            >
              <>#{channel.name}</>
            </Button>
          ))}
      </FlexRow>
    </Styled.Container>
  );
}

export default SearchOptions;
