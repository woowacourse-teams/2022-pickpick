import styled, { css } from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.nav`
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-around;
  padding: 5px;
  z-index: 999;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.SECONDARY}};
  `}
`;

export const LogoutButtonContainer = styled.div`
  position: fixed;
  bottom: 90px;
  right: 10px;
  z-index: 2;
`;
