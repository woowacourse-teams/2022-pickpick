import styled, { css } from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 10px 14px;
  width: 100%;
  border-radius: 4px;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.SECONDARY};
  `}
`;

export const Input = styled.input`
  display: inline-block;
  outline: none;
  border: none;
  width: 100%;

  ${({ theme }: StyledDefaultProps) => css`
    color: ${theme.COLOR.TEXT.DEFAULT};
    font-size: ${theme.FONT_SIZE.PLACEHOLDER};
  `}

  &::placeholder {
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.PLACEHOLDER};
      font-size: ${theme.FONT_SIZE.PLACEHOLDER};
    `}
  }
`;
