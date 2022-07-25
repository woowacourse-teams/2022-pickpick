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
  cursor: pointer;
  background-color: inherit;

  ${({ kind }: { kind: Kind }) => kindTable[kind]}
`;
