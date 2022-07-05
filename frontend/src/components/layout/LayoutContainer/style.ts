import styled from "styled-components";
import { Theme } from "@src/@types/shared";

export const Container = styled.div``;

export const Main = styled.main`
  padding: 0 20px;
  background-color: ${({ theme }: { theme: Theme }) =>
    theme.COLOR.BACKGROUND.PRIMARY};
`;
