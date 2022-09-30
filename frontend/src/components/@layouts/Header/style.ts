import styled, { css } from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.header`
  display: flex;
  align-items: flex-start;
  justify-content: space-between;

  padding: 20px;
  background-color: ${({ theme }: StyledDefaultProps) =>
    theme.COLOR.BACKGROUND.SECONDARY};

  position: fixed;
  top: 0;
  left: 0;
  right: 0;
`;

export const Title = styled.h1`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.TITLE};
    font-family: ${theme.FONT.SECONDARY};
    color: ${theme.COLOR.TEXT.DEFAULT};
  `}
`;
