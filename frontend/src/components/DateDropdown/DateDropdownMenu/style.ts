import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.ul`
  width: 125px;
  padding: 8px 12px;
  border-radius: 4px;

  position: absolute;
  top: 22px;
  left: 0;
  box-shadow: 0.5px 0.5px 2px 0px rgba(0, 0, 0, 0.1);

  z-index: 1;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.CONTAINER.DEFAULT};
  `}
`;

export const Option = styled.li`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.BODY};
  `}

  & + & {
    margin-top: 7px;
  }
`;

export const Button = styled.button`
  width: 100%;
  border: none;
  background-color: inherit;
  text-align: left;
  cursor: pointer;
`;
