import styled, { css, keyframes } from "styled-components";

import { SNACKBAR_STATUS } from "@src/@constants";
import { StyledDefaultProps } from "@src/@types/shared";
import { SnackbarStatus } from "@src/@types/shared";

interface StyledProps extends StyledDefaultProps {
  status: SnackbarStatus;
  isAlive: boolean;
}

const fadeIn = keyframes`
    from {
      bottom: 0;
      opacity: 0;
    }
    to {
      bottom: 90px;
      opacity: 1;
    }
`;

const fadeOut = keyframes`
from {
  bottom: 90px;
  opacity: 1;
}
to {
  bottom: 0;
  opacity: 0;
}
`;

export const Container = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;

  position: fixed;
  left: 50%;
  bottom: 90px;

  width: 300px;
  min-height: 60px;
  padding: 16px;
  border-radius: 8px;

  transform: translate(-50%, 0);
  z-index: 3;

  ${({ theme, status, isAlive }: StyledProps) => css`
    color: ${theme.COLOR.TEXT.WHITE};
    background-color: ${status === SNACKBAR_STATUS.SUCCESS
      ? theme.COLOR.CONTAINER.LIGHT_BLUE
      : theme.COLOR.CONTAINER.LIGHT_RED};

    animation: ${isAlive ? fadeIn : fadeOut} 0.7s;
  `}
`;
