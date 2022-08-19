import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";
import { SnackbarStatus } from "@src/@types/shared";
import { SNACKBAR_STATUS } from "@src/@constants";

interface StyledProps extends StyledDefaultProps {
  status: SnackbarStatus;
}

export const Container = styled.div`
  position: fixed;
  bottom: 100px;
  left: 50%;
  transform: translate(-50%, 0);
  width: 300px;
  min-height: 60px;
  padding: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 3;

  ${({ theme, status }: StyledProps) => css`
    color: ${theme.COLOR.TEXT.WHITE};
    background-color: ${status === SNACKBAR_STATUS.SUCCESS
      ? theme.COLOR.CONTAINER.LIGHT_BLUE
      : theme.COLOR.CONTAINER.LIGHT_RED};
  `}
`;
