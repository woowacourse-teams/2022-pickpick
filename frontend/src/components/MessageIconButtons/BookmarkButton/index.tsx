import { MouseEventHandler } from "react";
import IconButton from "@src/components/@shared/IconButton";
import StarIcon from "@public/assets/icons/StarIcon.svg";
import { FlexRow } from "@src/@styles/shared";
import * as Styled from "../style";

interface Props {
  isActive: boolean;
  onClick: MouseEventHandler;
}

function BookmarkButton({ isActive, onClick }: Props) {
  return (
    <IconButton type="button" icon="star" isActive={isActive} onClick={onClick}>
      <FlexRow justifyContent="center" alignItems="center" gap="5px">
        <Styled.ButtonText>북마크</Styled.ButtonText>
        <StarIcon width="12px" height="13.3px" fill="#ffffff" />
      </FlexRow>
    </IconButton>
  );
}

export default BookmarkButton;
