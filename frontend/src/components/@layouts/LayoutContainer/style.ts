import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

interface StyledProps extends StyledDefaultProps {
  hasMarginTop: boolean;
}

export const Main = styled.main`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  height: auto;
  padding-bottom: 100px;

  ${({ theme, hasMarginTop }: StyledProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.PRIMARY};
    margin-top: ${hasMarginTop ? "74.5px" : 0};
  `}
`;
