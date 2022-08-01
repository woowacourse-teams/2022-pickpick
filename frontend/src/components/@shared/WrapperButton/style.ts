import styled, { css, CSSProp } from "styled-components";
import { Kind } from ".";

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

  ${({ kind, isFuture }: { kind: Kind; isFuture?: boolean }) => css`
    opacity: ${isFuture ? 0.3 : 1};
    cursor: ${isFuture ? "default" : "pointer"};

    ${kindTable[kind]};
  `}
`;
