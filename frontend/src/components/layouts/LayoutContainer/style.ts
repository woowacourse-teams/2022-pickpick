import styled, { css } from "styled-components";
import { Theme } from "@src/@types/shared";

export const Container = styled.div``;

export const Main = styled.main`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: auto;

  ${({ theme, hasMarginTop }: { theme: Theme; hasMarginTop: boolean }) => css`
    background-color: ${theme.COLOR.BACKGROUND.PRIMARY};
    margin-top: ${hasMarginTop ? "74.5px" : 0};
  `}
`;
