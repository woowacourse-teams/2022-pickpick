import { MouseEventHandler } from "react";

import IconButton from "@src/components/@shared/IconButton";
import StarIcon from "@src/components/@svgIcons/StarIcon";

import { FlexRow } from "@src/@styles/shared";

import * as Styled from "../@styles/style";

interface Props {
  isActive: boolean;
  onClick: MouseEventHandler;
}

function BookmarkButton({ isActive, onClick }: Props) {
  return (
    <IconButton
      type="button"
      icon="star"
      isActive={isActive}
      onClick={onClick}
      tabIndex={0}
    >
      <FlexRow justifyContent="center" alignItems="center" gap="5px">
        <Styled.ButtonText>북마크</Styled.ButtonText>
        <StarIcon width="12px" height="13.3px" fill="#ffffff" />
      </FlexRow>
    </IconButton>
  );
}

export default BookmarkButton;
