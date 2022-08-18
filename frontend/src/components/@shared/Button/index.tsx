import { PropsWithChildren, ButtonHTMLAttributes } from "react";
import * as Styled from "./style";

export type Size = "small" | "medium" | "large";

export interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  size: Size;
  isActive: boolean;
}

function Button({ children, ...props }: PropsWithChildren<Props>) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

Button.defaultProps = {
  size: "large",
  isActive: true,
};

export default Button;
