import * as Styled from "./style";
import PlusIcon from "@public/assets/icons/PlusIcon.svg";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import WrapperLink from "@src/components/@shared/WrapperLink";
import { PATH_NAME } from "@src/@constants";
import { SubscribedChannel, Theme } from "@src/@types/shared";
import { useTheme } from "styled-components";
import ThemeToggler from "@src/components/ThemeToggler";
import { useParams } from "react-router-dom";
interface Props {
  channels?: SubscribedChannel[];
  handleCloseDrawer: () => void;
}

function Drawer({ channels = [], handleCloseDrawer }: Props) {
  const { channelId } = useParams();
  const theme = useTheme() as Theme;

  return (
    <Styled.Container>
      <FlexRow
        justifyContent="space-between"
        alignItems="center"
        padding="0 20px"
      >
        <Styled.Title>채널</Styled.Title>

        <WrapperLink to={PATH_NAME.ADD_CHANNEL}>
          {() => (
            <PlusIcon
              width="14px"
              height="14px"
              fill={theme.COLOR.TEXT.DEFAULT}
            />
          )}
        </WrapperLink>
      </FlexRow>

      <Styled.Hr />

      <FlexColumn gap="9px" padding="0 16px">
        {channels.map((channel, index) => (
          <WrapperLink key={channel.id} to={`${PATH_NAME.FEED}/${channel.id}`}>
            {({ isActive }) => (
              <Styled.ChannelName
                isActive={
                  isActive ||
                  (index === 0 && (!channelId || channelId === "main"))
                }
                onClick={handleCloseDrawer}
              >
                #{channel.name}
              </Styled.ChannelName>
            )}
          </WrapperLink>
        ))}
      </FlexColumn>

      <Styled.ThemeTogglerContainer>
        <ThemeToggler />
      </Styled.ThemeTogglerContainer>
    </Styled.Container>
  );
}

export default Drawer;
