import * as Styled from "./style";

export type Icon = "star" | "alarm";

export interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: JSX.Element | string;
  icon: Icon;
  isActive: boolean;
}

function IconButton({ children, ...props }: Props) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

export default IconButton;
