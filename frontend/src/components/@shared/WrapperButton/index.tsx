import { PropsWithChildren, ButtonHTMLAttributes } from "react";
import * as Styled from "./style";

export type Kind = "bigIcon" | "smallIcon" | "text";

export interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  kind: Kind;
  isFuture?: boolean;
}

function WrapperButton({ children, ...props }: PropsWithChildren<Props>) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

export default WrapperButton;
