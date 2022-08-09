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
  defaultChannel: { name: defaultName, id: defaultId },
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
          key={defaultName}
          isActive={channelIds.includes(defaultId)}
          size="small"
          onClick={() => handleToggleChannelId(defaultId)}
        >
          <>#{defaultName}</>
        </Button>

        {data.channels
          .filter((channel) => channel.id !== defaultId)
          .map(({ name, id }) => (
            <Button
              key={name}
              isActive={channelIds.includes(id)}
              size="small"
              onClick={() => handleToggleChannelId(id)}
            >
              <>#{name}</>
            </Button>
          ))}
      </Styled.ScrollContainer>
    </Styled.Container>
  );
}

export default SearchOptions;
