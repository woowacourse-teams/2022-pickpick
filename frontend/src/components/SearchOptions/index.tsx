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
      <Styled.Text>검색에 포함할 채널을 선택해주세요.</Styled.Text>

      <Styled.ScrollContainer>
        <Button
          isActive={channelIds.length === data.channels.length}
          size="small"
          onClick={handleToggleAllChannelIds}
        >
          {channelIds.length === data.channels.length
            ? "전체 해제"
            : "전체 선택"}
        </Button>

        <Button
          key={defaultChannel.name}
          isActive={channelIds.includes(defaultChannel.id)}
          size="small"
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
              size="small"
              onClick={() => handleToggleChannelId(channel.id)}
            >
              <>#{channel.name}</>
            </Button>
          ))}
      </Styled.ScrollContainer>
    </Styled.Container>
  );
}

export default SearchOptions;
