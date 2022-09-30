import styled, { css } from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.nav`
  display: flex;
  justify-content: space-around;

  padding: 5px;

  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;

  z-index: 10;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.SECONDARY}};
  `}
`;

export const LogoutButtonContainer = styled.div`
  position: fixed;
  right: 10px;
  bottom: 90px;

  z-index: 2;
`;
