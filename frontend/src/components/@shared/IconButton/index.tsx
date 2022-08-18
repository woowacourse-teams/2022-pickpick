import { PropsWithChildren, ButtonHTMLAttributes } from "react";
import * as Styled from "./style";

export type Icon = "star" | "alarm";

export interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  icon: Icon;
  isActive: boolean;
}

function IconButton({ children, ...props }: PropsWithChildren<Props>) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

export default IconButton;
