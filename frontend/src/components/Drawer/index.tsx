import * as Styled from "./style";
import PlusIcon from "@public/assets/icons/PlusIcon.svg";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import WrapperLink from "@src/components/@shared/WrapperLink";
import { PATH_NAME } from "@src/@constants";
import { SubscribedChannel, Theme } from "@src/@types/shared";
import { Link } from "react-router-dom";
import { useTheme } from "styled-components";
import ThemeToggler from "@src/components/ThemeToggler";
import SunIcon from "@public/assets/icons/SunIcon.svg";
interface Props {
  channels?: SubscribedChannel[];
  handleCloseDrawer: () => void;
}

function Drawer({ channels = [], handleCloseDrawer }: Props) {
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
      <FlexColumn gap="11px" padding="0 20px">
        {channels.map((channel) => (
          <Link key={channel.id} to={`${PATH_NAME.FEED}/${channel.id}`}>
            <Styled.ChannelName onClick={handleCloseDrawer}>
              #{channel.name}
            </Styled.ChannelName>
          </Link>
        ))}
      </FlexColumn>
      <Styled.ThemeTogglerContainer>
        <ThemeToggler />
      </Styled.ThemeTogglerContainer>
    </Styled.Container>
  );
}

export default Drawer;
