import styled, { css } from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.header`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: ${({ theme }: StyledDefaultProps) =>
    theme.COLOR.BACKGROUND.SECONDARY};
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
`;

export const Title = styled.h1`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.TITLE};
    font-family: ${theme.FONT.SECONDARY};
    color: ${theme.COLOR.TEXT.DEFAULT};
  `}
`;
