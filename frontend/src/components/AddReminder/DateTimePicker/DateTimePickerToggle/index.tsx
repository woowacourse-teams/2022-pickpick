import ArrowIconDown from "@src/components/@svgIcons/ArrowIconDown";

import { FlexRow } from "@src/@styles/shared";
import { StrictPropsWithChildren } from "@src/@types/utils";

import * as Styled from "./style";

interface Props {
  text: string;
  handleToggleDropdown: VoidFunction;
}

function DateTimePickerToggle({
  text,
  handleToggleDropdown,
  children,
}: StrictPropsWithChildren<Props>) {
  return (
    <Styled.Container onClick={handleToggleDropdown}>
      <FlexRow alignItems="center" gap="8px">
        {children}
        <Styled.Text>{text}</Styled.Text>
      </FlexRow>

      <ArrowIconDown width="24px" height="24px" fill="#8B8B8B" />
    </Styled.Container>
  );
}

export default DateTimePickerToggle;
