import { Theme } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.footer`
  padding: 20px;
  background-color: ${({ theme }: { theme: Theme }) =>
    theme.COLOR.BACKGROUND.PRIMARY};
`;

export const Description = styled.p`
  ${({ theme }: { theme: Theme }) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    color: ${theme.COLOR.SECONDARY.DEFAULT};
  `}
`;

export const IconContainer = styled.div`
  display: flex;
  gap: 5px;
  margin-top: 5px;
`;
