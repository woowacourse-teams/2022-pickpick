import { useTheme } from "styled-components";

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
  const theme = useTheme();

  return (
    <Styled.Container onClick={handleToggleDropdown}>
      <FlexRow alignItems="center" gap="8px">
        {children}
        <Styled.Text
          aria-live="assertive"
          aria-label={`${text}를 선택했습니다.`}
        >
          {text}
        </Styled.Text>
      </FlexRow>

      <ArrowIconDown
        width="24px"
        height="24px"
        fill={theme.COLOR.SECONDARY.DEFAULT}
      />
    </Styled.Container>
  );
}

export default DateTimePickerToggle;
