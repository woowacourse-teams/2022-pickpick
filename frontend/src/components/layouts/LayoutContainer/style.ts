import styled from "styled-components";
import { Theme } from "@src/@types/shared";

export const Container = styled.div``;

export const Main = styled.main`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0 20px;
  margin-top: 74.5px;
  height: auto;
  min-height: 100vh;
  background-color: ${({ theme }: { theme: Theme }) =>
    theme.COLOR.BACKGROUND.PRIMARY};
`;
