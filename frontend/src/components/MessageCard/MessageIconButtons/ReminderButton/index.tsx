import { MouseEventHandler } from "react";

import IconButton from "@src/components/@shared/IconButton";
import ReminderIconActive from "@src/components/@svgIcons/ReminderIconActive";

import { FlexRow, SrOnlyDescription } from "@src/@styles/shared";

import * as Styled from "../@styles/style";

interface Props {
  isActive: boolean;
  onClick: MouseEventHandler;
}

function ReminderButton({ isActive, onClick }: Props) {
  return (
    <IconButton
      type="button"
      icon="alarm"
      isActive={isActive}
      onClick={onClick}
    >
      <SrOnlyDescription>
        {isActive
          ? "리마인더가 설정되어 있습니다. 수정하시려면 클릭해주세요."
          : "리마인더가 설정되어 있지 않습니다. 설정하시려면 클릭해주세요."}
      </SrOnlyDescription>

      <FlexRow justifyContent="center" alignItems="center" gap="5px">
        <Styled.ButtonText>리마인더</Styled.ButtonText>
        <ReminderIconActive width="12px" height="12px" fill="#ffffff" />
      </FlexRow>
    </IconButton>
  );
}

export default ReminderButton;
