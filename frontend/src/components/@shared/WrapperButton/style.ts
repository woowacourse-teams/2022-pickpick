import styled, { CSSProp, css } from "styled-components";

import { Kind, Props } from ".";

type StyledProps = Omit<Props, "children">;

export const kindTable: Record<Kind, CSSProp> = {
  bigIcon: css`
    padding: 20px;
  `,
  smallIcon: css`
    padding: 0;
  `,
  text: css`
    padding: 4px 12px;
  `,
};

export const Container = styled.button`
  border: none;
  background-color: inherit;

  ${({ kind, disabled }: StyledProps) => css`
    opacity: ${disabled ? 0.3 : 1};
    cursor: ${disabled ? "default" : "pointer"};

    ${kindTable[kind]};
  `}
`;
