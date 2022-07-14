import styled from "styled-components";
import { Theme } from "@src/@types/shared";

export const Container = styled.nav`
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 3;
  height: 62px;

  display: flex;
  justify-content: space-around;
  padding: 5px;
  background-color: ${({ theme }: { theme: Theme }) =>
    `${theme.COLOR.BACKGROUND.SECONDARY}`};
`;
