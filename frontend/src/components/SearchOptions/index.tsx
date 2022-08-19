import { SubscribedChannel } from "@src/@types/shared";
import Button from "@src/components/@shared/Button";
import * as Styled from "./style";

interface Props {
  allChannels: SubscribedChannel[];
  currentChannels: SubscribedChannel[];
  remainingChannels: SubscribedChannel[];
  selectedChannelIds: number[];
  handleToggleChannel: (id: number) => void;
  handleToggleAllChannels: () => void;
}

function SearchOptions({
  allChannels,
  currentChannels,
  remainingChannels,
  selectedChannelIds,
  handleToggleChannel,
  handleToggleAllChannels,
}: Props) {
  return (
    <Styled.Container>
      <Styled.Text>검색에 포함 할 채널을 선택해주세요.</Styled.Text>

      <Styled.ScrollContainer>
        <Button
          isActive={selectedChannelIds.length === allChannels.length}
          size="small"
          onClick={handleToggleAllChannels}
          type="button"
        >
          {selectedChannelIds.length === allChannels.length
            ? "전체 해제"
            : "전체 선택"}
        </Button>

        {currentChannels.map(({ name, id }) => (
          <Button
            key={id}
            isActive={selectedChannelIds.includes(id)}
            size="small"
            onClick={() => handleToggleChannel(id)}
            type="button"
          >
            <>#{name}</>
          </Button>
        ))}

        {remainingChannels.map(({ name, id }) => (
          <Button
            key={id}
            isActive={selectedChannelIds.includes(id)}
            size="small"
            onClick={() => handleToggleChannel(id)}
            type="button"
          >
            <>#{name}</>
          </Button>
        ))}
      </Styled.ScrollContainer>
    </Styled.Container>
  );
}

export default SearchOptions;
