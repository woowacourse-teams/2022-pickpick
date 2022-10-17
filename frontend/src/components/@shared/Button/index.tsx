import { ButtonHTMLAttributes } from "react";

import { StrictPropsWithChildren } from "@src/@types/utils";

import * as Styled from "./style";

export type StyleType = "primary" | "secondary" | "tertiary";
export type Size = "small" | "medium" | "large";

export interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  size: Size;
  styleType: StyleType;
}

function Button({ children, ...props }: StrictPropsWithChildren<Props>) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

Button.defaultProps = {
  size: "large",
  styleType: "primary",
};

export default Button;
