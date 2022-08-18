import { MouseEventHandler } from "react";
import { FlexRow } from "@src/@styles/shared";
import IconButton from "@src/components/@shared/IconButton";
import * as Styled from "../style";
import ReminderIconActive from "@public/assets/icons/ReminderIcon-Active.svg";

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
      <FlexRow justifyContent="center" alignItems="center" gap="5px">
        <Styled.ButtonText>리마인더</Styled.ButtonText>
        <ReminderIconActive width="12px" height="12px" fill="#ffffff" />
      </FlexRow>
    </IconButton>
  );
}

export default ReminderButton;
