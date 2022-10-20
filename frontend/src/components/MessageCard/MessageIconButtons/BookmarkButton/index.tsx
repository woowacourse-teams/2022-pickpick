import { MouseEventHandler } from "react";

import IconButton from "@src/components/@shared/IconButton";
import StarIcon from "@src/components/@svgIcons/StarIcon";

import { FlexRow, SrOnlyDescription } from "@src/@styles/shared";

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
      <SrOnlyDescription>
        {isActive
          ? "북마크가 설정되어 있습니다. 북마크를 해제하시려면 클릭해주세요."
          : "북마크가 설정되어 있지 않습니다. 북마크를 설정하시려면 클릭해주세요."}
      </SrOnlyDescription>

      <FlexRow justifyContent="center" alignItems="center" gap="5px">
        <Styled.ButtonText>북마크</Styled.ButtonText>
        <StarIcon width="12px" height="13.3px" fill="#ffffff" />
      </FlexRow>
    </IconButton>
  );
}

export default BookmarkButton;
