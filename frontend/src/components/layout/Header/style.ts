import styled, { css } from "styled-components";
import { Theme } from "@src/@types/shared";

export const Container = styled.header`
  background-color: ${({ theme }: { theme: Theme }) =>
    theme.COLOR.BACKGROUND.SECONDARY};
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
`;

export const Title = styled.h1`
  ${({ theme }: { theme: Theme }) => css`
    font-size: ${theme.FONT_SIZE.TITLE};
    font-family: ${theme.FONT.SECONDARY};
    color: ${theme.COLOR.TEXT.DEFAULT};
  `}
`;
