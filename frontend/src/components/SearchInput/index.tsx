import React from "react";
import * as Styled from "./style";
import SearchIcon from "@public/assets/icons/SearchIcon.svg";
import { FlexRow } from "@src/@styles/shared";

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {
  children?: JSX.Element | string;
}

function SearchInput({ children, ...props }: Props) {
  return (
    <Styled.Container>
      <FlexRow gap="6px" marginBottom="10px" alignItems="center">
        <SearchIcon width="20px" height="20px" fill="#8B8B8B" />
        <Styled.Input {...props} />
      </FlexRow>
      {children}
    </Styled.Container>
  );
}

export default SearchInput;
