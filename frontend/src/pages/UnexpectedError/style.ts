import { Theme } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Title = styled.h1`
  text-align: center;

  ${({ theme }: { theme: Theme }) => css`
    font-size: ${theme.FONT_SIZE.TITLE};
  `}
`;

export const Description = styled.p`
  text-align: center;

  ${({ theme }: { theme: Theme }) => css`
    font-size: ${theme.FONT_SIZE.X_LARGE_BODY};
  `}
`;
