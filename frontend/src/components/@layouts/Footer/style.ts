import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.footer`
  padding: 20px 20px 77.5px 20px;
  background-color: ${({ theme }: StyledDefaultProps) =>
    theme.COLOR.BACKGROUND.PRIMARY};
`;

export const Description = styled.p`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    color: ${theme.COLOR.SECONDARY.DEFAULT};
  `}
`;

export const IconContainer = styled.div`
  display: flex;
  gap: 5px;
  margin-top: 5px;
`;
