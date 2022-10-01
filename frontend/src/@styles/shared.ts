import { CSSProperties } from "react";
import styled, { css } from "styled-components";

type FlexStyle = Omit<CSSProperties, "display" | "flexDirection">;

export const FlexRow = styled.div`
  display: flex;

  ${(props: FlexStyle) => css`
    ${props}
  `}
`;

export const FlexColumn = styled.div`
  display: flex;
  flex-direction: column;

  ${(props: FlexStyle) => css`
    ${props}
  `}
`;
