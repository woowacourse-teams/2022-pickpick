import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";
import { Props } from ".";

interface StyledProps extends StyledDefaultProps {
  hasBackgroundColor: boolean;
}

export const Container = styled.div<Props>`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1;

  ${({ theme, hasBackgroundColor }: StyledProps) => css`
    background-color: ${hasBackgroundColor
      ? theme.COLOR.DIMMER
      : "transparent"};
  `};
`;
