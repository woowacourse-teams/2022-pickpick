import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

import { Props } from ".";

const iconColorTable = {
  remove: css`
    background-color: ${({ theme }: StyledDefaultProps) =>
      theme.COLOR.CONTAINER.LIGHT_RED};
  `,
  alarm: css`
    background-color: ${({ theme }: StyledDefaultProps) =>
      theme.COLOR.CONTAINER.BLUE};
  `,
  star: css`
    background-color: ${({ theme }: StyledDefaultProps) =>
      theme.COLOR.CONTAINER.LIGHT_ORANGE};
  `,
};

const inactiveStyle = css`
  background-color: ${({ theme }: StyledDefaultProps) =>
    theme.COLOR.BACKGROUND.TERTIARY};
`;

export const Container = styled.button`
  border: none;
  border-radius: 4px;
  padding: 4px 12px;
  cursor: pointer;

  ${({ icon, isActive }: Pick<Props, "icon" | "isActive">) => css`
    ${isActive ? iconColorTable[icon] : inactiveStyle}
  `};
`;
