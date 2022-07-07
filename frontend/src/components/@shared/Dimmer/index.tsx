import * as Styled from "./style";

export interface Props {
  hasBackgroundColor: boolean;
  onClick: () => void;
}

function Dimmer(props: Props) {
  return <Styled.Container {...props} />;
}

export default Dimmer;
