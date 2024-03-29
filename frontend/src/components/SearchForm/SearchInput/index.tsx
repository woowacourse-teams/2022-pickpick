import { InputHTMLAttributes } from "react";

import SearchIcon from "@src/components/@svgIcons/SearchIcon";

import { FlexRow } from "@src/@styles/shared";
import { StrictPropsWithChildren } from "@src/@types/utils";

import * as Styled from "./style";

function SearchInput({
  children,
  ...props
}: StrictPropsWithChildren<InputHTMLAttributes<HTMLInputElement>>) {
  return (
    <Styled.Container>
      <FlexRow gap="6px" alignItems="center">
        <SearchIcon width="20px" height="20px" fill="#8B8B8B" />
        <Styled.Input {...props} />
        <Styled.SearchButton type="submit">검색</Styled.SearchButton>
      </FlexRow>
      {children}
    </Styled.Container>
  );
}

export default SearchInput;
