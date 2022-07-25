import * as Styled from "./style";
import ArrowIconDown from "@public/assets/icons/ArrowIcon-Down.svg";
import { FlexRow } from "@src/@styles/shared";

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  text: string;
}

function DateDropdownToggle({ text, ...props }: Props) {
  return (
    <Styled.Container {...props}>
      <FlexRow gap="8px">
        <Styled.Text>{text}</Styled.Text>
        <ArrowIconDown width="12px" height="12px" fill="#8B8B8B" />
      </FlexRow>
    </Styled.Container>
  );
}

export default DateDropdownToggle;
