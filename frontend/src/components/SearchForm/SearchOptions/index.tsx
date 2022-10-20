import { memo } from "react";

import Button from "@src/components/@shared/Button";

import { SubscribedChannel } from "@src/@types/api";

import * as Styled from "./style";

interface Props {
  allChannels: SubscribedChannel[];
  currentChannels: SubscribedChannel[];
  remainingChannels: SubscribedChannel[];
  selectedChannelIds: number[];
  handleToggleChannel: (id: number) => void;
  handleToggleAllChannels: VoidFunction;
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
      <Styled.Text tabIndex={0}>
        검색에 포함 할 채널을 선택해주세요.
      </Styled.Text>

      <Styled.ScrollContainer>
        <Button
          styleType={
            selectedChannelIds.length === allChannels.length
              ? "primary"
              : "tertiary"
          }
          size="small"
          onClick={handleToggleAllChannels}
          type="button"
          aria-label={`${
            selectedChannelIds.length === allChannels.length
              ? "전체 선택을 해제하려면 클릭해주세요."
              : "전체 선택을 하려면 클릭해주세요."
          }`}
        >
          {selectedChannelIds.length === allChannels.length
            ? "전체 해제"
            : "전체 선택"}
        </Button>

        {currentChannels.map(({ name, id }) => (
          <Button
            key={id}
            styleType={selectedChannelIds.includes(id) ? "primary" : "tertiary"}
            size="small"
            onClick={() => handleToggleChannel(id)}
            type="button"
            aria-label={`${
              selectedChannelIds.includes(id)
                ? `${name} 채널을 검색에 포함하지 않으려면 클릭해주세요.`
                : `${name} 채널을 검색에 포함하려면 클릭해주세요.`
            }`}
          >
            <>#{name}</>
          </Button>
        ))}

        {remainingChannels.map(({ name, id }) => (
          <Button
            key={id}
            styleType={selectedChannelIds.includes(id) ? "primary" : "tertiary"}
            size="small"
            onClick={() => handleToggleChannel(id)}
            type="button"
            aria-label={`${
              selectedChannelIds.includes(id)
                ? `${name} 채널을 검색에 포함하지 않으려면 클릭해주세요.`
                : `${name} 채널을 검색에 포함하려면 클릭해주세요.`
            }`}
          >
            <>#{name}</>
          </Button>
        ))}
      </Styled.ScrollContainer>
    </Styled.Container>
  );
}

export default memo(SearchOptions);
