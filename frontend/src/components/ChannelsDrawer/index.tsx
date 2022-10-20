import { useParams } from "react-router-dom";
import { useTheme } from "styled-components";

import WrapperNavLink from "@src/components/@shared/WrapperNavLink";
import PlusIcon from "@src/components/@svgIcons/PlusIcon";
import ThemeToggler from "@src/components/ThemeToggler";

import { DEFAULT_CHANNEL_ID } from "@src/@constants/api";
import { PATH_NAME } from "@src/@constants/path";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import { SubscribedChannel } from "@src/@types/api";
import { Theme } from "@src/@types/shared";

import * as Styled from "./style";

interface Props {
  channels?: SubscribedChannel[];
  handleCloseDrawer: VoidFunction;
}

function ChannelsDrawer({ channels = [], handleCloseDrawer }: Props) {
  const { channelId } = useParams();
  const theme = useTheme() as Theme;

  return (
    <Styled.Container>
      <FlexRow
        justifyContent="space-between"
        alignItems="center"
        padding="0 20px"
      >
        <Styled.Title aria-label="채널 변경">채널</Styled.Title>

        <WrapperNavLink to={PATH_NAME.ADD_CHANNEL}>
          {() => (
            <Styled.Button
              autoFocus
              onClick={handleCloseDrawer}
              aria-label="채널 추가하러 가기"
            >
              <PlusIcon
                width="14px"
                height="14px"
                fill={theme.COLOR.TEXT.DEFAULT}
              />
            </Styled.Button>
          )}
        </WrapperNavLink>
      </FlexRow>

      <Styled.Hr />

      <FlexColumn gap="9px" padding="0 16px">
        {channels.map((channel, index) => (
          <WrapperNavLink
            key={channel.id}
            to={`${PATH_NAME.FEED}/${channel.id}`}
          >
            {({ isActive }) => (
              <Styled.Button
                type="button"
                onClick={handleCloseDrawer}
                aria-label={`${channel.name} 이동하기`}
              >
                <Styled.ChannelName
                  isActive={
                    isActive ||
                    (index === 0 &&
                      (!channelId || channelId === DEFAULT_CHANNEL_ID))
                  }
                >
                  #{channel.name}
                </Styled.ChannelName>
              </Styled.Button>
            )}
          </WrapperNavLink>
        ))}
      </FlexColumn>

      <Styled.ThemeTogglerContainer>
        <ThemeToggler />
      </Styled.ThemeTogglerContainer>
    </Styled.Container>
  );
}

export default ChannelsDrawer;
