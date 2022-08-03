import styled from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.nav`
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-around;
  padding: 5px;
  z-index: 1;
  background-color: ${({ theme }: StyledDefaultProps) =>
    `${theme.COLOR.BACKGROUND.SECONDARY}`};
`;

export const LogoutButtonContainer = styled.div`
  position: fixed;
  bottom: 90px;
  right: 10px;
`;
