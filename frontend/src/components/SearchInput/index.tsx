import React from "react";
import * as Styled from "./style";
import SearchIcon from "@public/assets/icons/SearchIcon.svg";

function SearchInput(props: React.InputHTMLAttributes<HTMLInputElement>) {
  return (
    <Styled.Container>
      <SearchIcon width="20px" height="20px" fill="#8B8B8B" />
      <Styled.Input {...props} />
    </Styled.Container>
  );
}

export default SearchInput;
