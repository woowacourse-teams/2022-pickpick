import styled from "styled-components";
import { Theme } from "@src/@types/shared";

export const Container = styled.nav`
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;

  display: flex;
  gap: 38px;
  padding: 25px;
  background-color: ${({ theme }: { theme: Theme }) =>
    `${theme.COLOR.BACKGROUND.SECONDARY}`};
`;
