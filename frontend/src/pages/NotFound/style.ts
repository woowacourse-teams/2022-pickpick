import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

export const Title = styled.h1`
  text-align: center;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.TITLE};
  `}
`;

export const Description = styled.p`
  text-align: center;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.X_LARGE_BODY};
  `}
`;
