import { ButtonHTMLAttributes } from "react";

import { StrictPropsWithChildren } from "@src/@types/utils";

import * as Styled from "./style";

export type Icon = "star" | "alarm";

export interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  icon: Icon;
  isActive: boolean;
}

function IconButton({ children, ...props }: StrictPropsWithChildren<Props>) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

export default IconButton;
