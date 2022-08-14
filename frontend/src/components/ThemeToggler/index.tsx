import React from "react";
import * as Styled from "./style";

type Props = Omit<React.InputHTMLAttributes<HTMLInputElement>, "type">;

function ThemeToggler(props: Props) {
  return <Styled.Container type="checkbox" {...props} />;
}

export default ThemeToggler;
