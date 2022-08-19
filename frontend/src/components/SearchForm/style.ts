import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.form`
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 16px 34px;
  width: 100%;
  border-radius: 4px;
  position: fixed;
  width: 100%;
  max-width: 960px;
  box-shadow: 0.5px 0.5px 2px 0px rgba(0, 0, 0, 0.1);
  top: 0;
  left: 50%;
  right: 0;
  transform: translate(-50%);
  z-index: 2;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.SECONDARY};
  `}
`;
