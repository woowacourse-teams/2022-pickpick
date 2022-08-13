import styled, { css } from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.div``;

export const Input = styled.input`
  display: inline-block;
  outline: none;
  border: none;
  width: 100%;
  background-color: transparent;

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

export const SearchButton = styled.button`
  border: none;
  border-radius: 4px;
  white-space: nowrap;
  cursor: pointer;
  padding: 0.25rem 0.938rem;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    background-color: ${theme.COLOR.BACKGROUND.TERTIARY};
  `}

  &:hover {
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.WHITE};
      background-color: ${theme.COLOR.PRIMARY.DEFAULT};
    `}
  }
`;
