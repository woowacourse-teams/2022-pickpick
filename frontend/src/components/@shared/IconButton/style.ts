import { Theme } from "@src/@types/shared";
import styled, { css } from "styled-components";
import { Props } from ".";

const iconColorTable = {
  remove: css`
    background-color: ${({ theme }: { theme: Theme }) =>
      theme.COLOR.CONTAINER.LIGHT_RED};
  `,
  alarm: css`
    background-color: ${({ theme }: { theme: Theme }) =>
      theme.COLOR.CONTAINER.LIGHT_BLUE};
  `,
  star: css`
    background-color: ${({ theme }: { theme: Theme }) =>
      theme.COLOR.CONTAINER.LIGHT_ORANGE};
  `,
};

const backgroundColorGary = css`
  background-color: ${({ theme }: { theme: Theme }) =>
    theme.COLOR.BACKGROUND.TERTIARY};
`;

export const Container = styled.button`
  border: none;
  border-radius: 4px;
  padding: 4px 12px;
  cursor: pointer;

  ${({ icon, isActive }: Pick<Props, "icon" | "isActive">) => css`
    ${isActive ? iconColorTable[icon] : backgroundColorGary}
  `};
`;
