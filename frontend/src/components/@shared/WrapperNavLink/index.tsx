import { ReactNode } from "react";
import { NavLinkProps } from "react-router-dom";

import { Kind } from "@src/components/@shared/WrapperButton";

import { PropsWithFunctionChildren } from "@src/@types/utils";

import * as Styled from "./style";

interface Props extends NavLinkProps {
  kind?: Kind;
  children: ({ isActive }: { isActive: boolean }) => ReactNode;
}

function WrapperNavLink({
  children,
  ...props
}: PropsWithFunctionChildren<Props>) {
  return (
    <Styled.Container {...props}>
      {({ isActive }) => children({ isActive })}
    </Styled.Container>
  );
}

export default WrapperNavLink;
