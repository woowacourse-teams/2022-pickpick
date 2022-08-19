import { ReactNode } from "react";
import { NavLinkProps } from "react-router-dom";
import { Kind } from "../WrapperButton";
import * as Styled from "./style";

interface Props extends NavLinkProps {
  kind?: Kind;
  children: ({ isActive }: { isActive: boolean }) => ReactNode;
}

function WrapperLink({ children, ...props }: Props) {
  if (!children || typeof children !== "function") return null;

  return (
    <Styled.Container {...props}>
      {({ isActive }) => children({ isActive })}
    </Styled.Container>
  );
}

export default WrapperLink;
