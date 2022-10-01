import styled, { css } from "styled-components";

import { SNACKBAR_STATUS } from "@src/@constants";
import { StyledDefaultProps } from "@src/@types/shared";
import { SnackbarStatus } from "@src/@types/shared";

interface StyledProps extends StyledDefaultProps {
  status: SnackbarStatus;
}

export const Container = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;

  position: fixed;
  left: 50%;
  bottom: 100px;

  width: 300px;
  min-height: 60px;
  padding: 16px;
  border-radius: 8px;

  transform: translate(-50%, 0);
  z-index: 3;

  ${({ theme, status }: StyledProps) => css`
    color: ${theme.COLOR.TEXT.WHITE};
    background-color: ${status === SNACKBAR_STATUS.SUCCESS
      ? theme.COLOR.CONTAINER.LIGHT_BLUE
      : theme.COLOR.CONTAINER.LIGHT_RED};
  `}
`;
