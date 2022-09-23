import styled, { css } from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

interface StyledProps extends StyledDefaultProps {
  hasMarginTop: boolean;
}

export const Container = styled.div``;

export const Main = styled.main`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding-bottom: 100px;
  height: auto;

  ${({ theme, hasMarginTop }: StyledProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.PRIMARY};
    margin-top: ${hasMarginTop ? "74.5px" : 0};
  `}
`;
