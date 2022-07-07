import * as Styled from "./style";

export type Kind = "bigIcon" | "smallIcon" | "text";

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: JSX.Element | string;
  kind: Kind;
}

function WrapperButton({ children, ...props }: Props) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

export default WrapperButton;
