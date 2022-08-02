import * as Styled from "./style";
import PlusIcon from "@public/assets/icons/PlusIcon.svg";
import { FlexColumn, FlexRow } from "@src/@styles/shared";
import WrapperLink from "../@shared/WrapperLink";
import { PATH_NAME } from "@src/@constants";
import { SubscribedChannel } from "@src/@types/shared";
import { Link } from "react-router-dom";

interface Props {
  channels?: SubscribedChannel[];
}

function Drawer({ channels = [] }: Props) {
  return (
    <Styled.Container>
      <FlexRow
        justifyContent="space-between"
        alignItems="center"
        padding="0 20px"
      >
        <Styled.Title>채널</Styled.Title>
        <WrapperLink to={PATH_NAME.ADD_CHANNEL}>
          {() => <PlusIcon width="14px" height="14px" fill="#121212" />}
        </WrapperLink>
      </FlexRow>
      <Styled.Hr />
      <FlexColumn gap="11px" padding="0 20px">
        {channels.map((channel) => (
          <Link key={channel.id} to={`${PATH_NAME.FEED}/${channel.id}`}>
            <Styled.ChannelName>#{channel.name}</Styled.ChannelName>
          </Link>
        ))}
      </FlexColumn>
    </Styled.Container>
  );
}

export default Drawer;
