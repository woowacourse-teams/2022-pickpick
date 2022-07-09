import styled, { css } from "styled-components";
import { Theme } from "@src/@types/shared";

export const Container = styled.div`
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 14px;
  background-color: ${({ theme }: { theme: Theme }) =>
    theme.COLOR.BACKGROUND.SECONDARY};
  width: 100%;
  border-radius: 4px;
`;

export const Input = styled.input`
  display: inline-block;
  outline: none;
  border: none;
  width: 100%;
  ${({ theme }: { theme: Theme }) => css`
    color: ${theme.COLOR.TEXT.DEFAULT};
    font-size: ${theme.FONT_SIZE.PLACEHOLDER};
  `}

  &::placeholder {
    ${({ theme }: { theme: Theme }) => css`
      color: ${theme.COLOR.TEXT.PLACEHOLDER};
      font-size: ${theme.FONT_SIZE.PLACEHOLDER};
    `}
  }
`;
