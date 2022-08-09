import React from "react";
import * as Styled from "./style";

export type Size = "small" | "medium" | "large";

export interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: JSX.Element | string;
  size: Size;
  isActive: boolean;
  onClick?: () => void;
}

function Button({ children, ...props }: Props) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

Button.defaultProps = {
  size: "large",
  isActive: true,
};

export default Button;
