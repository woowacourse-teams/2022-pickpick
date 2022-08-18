import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 4px;
  padding: 2px 6px;
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
