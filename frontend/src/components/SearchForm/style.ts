import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.form`
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 10px 14px;
  width: 100%;
  border-radius: 4px;
  z-index: 1;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.SECONDARY};
  `}
`;
