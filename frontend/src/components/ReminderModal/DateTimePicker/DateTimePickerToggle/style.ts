import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;

  padding: 2px 6px;
  border-radius: 4px;
  cursor: pointer;

  ${({ theme }: StyledDefaultProps) => css`
    border: 1px solid ${theme.COLOR.BACKGROUND.TERTIARY};
  `}
`;

export const Text = styled.p`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
  `}
`;
