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

const srOnly = css`
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border-width: 0;
`;

export const SrOnlyTitle = styled.h1`
  ${srOnly}
`;
