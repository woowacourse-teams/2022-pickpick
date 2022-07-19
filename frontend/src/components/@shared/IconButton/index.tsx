import * as Styled from "./style";

export type Icon = "star" | "alarm" | "remove";

export interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: JSX.Element | string;
  icon: Icon;
  isActive: boolean;
  onClick: () => void;
}

function IconButton({ children, ...props }: Props) {
  return <Styled.Container {...props}>{children}</Styled.Container>;
}

export default IconButton;
