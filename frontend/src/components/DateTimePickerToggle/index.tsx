import { PropsWithChildren } from "react";
import DownArrowIcon from "@public/assets/icons/ArrowIcon-Down.svg";
import { FlexRow } from "@src/@styles/shared";
import * as Styled from "./style";

interface Props {
  text: string;
  handleToggleDropdown: () => void;
}

function DateTimePickerToggle({
  text,
  handleToggleDropdown,
  children,
}: PropsWithChildren<Props>) {
  return (
    <Styled.Container onClick={handleToggleDropdown}>
      <FlexRow alignItems="center" gap="8px">
        {children}
        <Styled.Text>{text}</Styled.Text>
      </FlexRow>

      <DownArrowIcon width="24px" height="24px" fill="#8B8B8B" />
    </Styled.Container>
  );
}

export default DateTimePickerToggle;
