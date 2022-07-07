import styled, { css } from "styled-components";
import { Theme } from "@src/@types/shared";

export const Container = styled.ul`
  box-shadow: 0.5px 0.5px 2px 0px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  width: 125px;
  padding: 8px 12px;
  position: absolute;
  top: 22px;
  left: 0;

  ${({ theme }: { theme: Theme }) => css`
    background-color: ${theme.COLOR.CONTAINER.DEFAULT};
  `}
`;

export const Option = styled.li`
  ${({ theme }: { theme: Theme }) => css`
    font-size: ${theme.FONT_SIZE.BODY};
  `}

  & + & {
    margin-top: 7px;
  }
`;

export const Button = styled.button`
  border: none;
  cursor: pointer;
  background-color: inherit;
  width: 100%;
  text-align: left;
`;
