import { ButtonHTMLAttributes } from "react";

import { StrictPropsWithChildren } from "@src/@types/utils";

import * as Styled from "./style";

export type Kind = "bigIcon" | "smallIcon" | "text";

export interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  kind: Kind;
}

function WrapperButton({ children, ...props }: StrictPropsWithChildren<Props>) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

export default WrapperButton;
