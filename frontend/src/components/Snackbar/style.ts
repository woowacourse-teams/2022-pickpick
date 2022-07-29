import { Theme } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.div`
  position: fixed;
  bottom: 100px;
  left: 50%;
  transform: translate(-50%, 0);
  width: 300px;
  min-height: 60px;
  padding: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;

  ${({ theme }: { theme: Theme }) => css`
    color: ${theme.COLOR.TEXT.WHITE};
    background-color: ${theme.COLOR.CONTAINER.LIGHT_RED};
  `}
`;
