import React from "react";
import styled, { css } from "styled-components";

type FlexStyle = Omit<React.CSSProperties, "display" | "flexDirection">;

export const FlexRow = styled.div`
  display: flex;

  ${(props: FlexStyle) => css`
    ${props}
  `}
`;

export const FlexColumn = styled.div<FlexStyle>`
  display: flex;
  flex-direction: column;

  ${(props: FlexStyle) => css`
    ${props}
  `}
`;
