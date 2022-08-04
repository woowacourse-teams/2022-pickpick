import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.button`
  display: flex;
  align-items: center;
  border: none;
  padding: 4px 12px;
  width: fit-content;
  background-color: ${({ theme }: StyledDefaultProps) =>
    theme.COLOR.BACKGROUND.SECONDARY};
  border-radius: 50px;
  cursor: pointer;
  box-shadow: 0.5px 0.5px 2px 0px rgba(0, 0, 0, 0.1);
`;

export const Text = styled.p`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    color: ${theme.COLOR.TEXT.DEFAULT};
  `}
`;
