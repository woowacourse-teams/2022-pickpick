import { LinkProps } from "react-router-dom";
import { Kind } from "../WrapperButton";
import * as Styled from "./style";

interface Props extends LinkProps {
  kind?: Kind;
  children: JSX.Element | string;
}

function WrapperLink(props: Props) {
  return <Styled.Container {...props} />;
}

export default WrapperLink;
