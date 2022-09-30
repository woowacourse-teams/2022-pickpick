import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

interface ChannelNameStyledProps extends StyledDefaultProps {
  isActive: boolean;
}

export const Container = styled.div`
  width: 228px;
  height: calc(100% - 78px);
  padding: 20px 0;
  border-radius: 0 4px 4px 0;

  position: fixed;
  top: 0;
  left: 0;

  z-index: 2;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.CONTAINER.DEFAULT};
  `}
`;

export const Hr = styled.hr`
  height: 1px;
  padding: 0 10px;
  border: 0;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BORDER};
  `}
`;

export const Title = styled.h1`
  font-weight: 600;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.LARGE_BODY};
  `}
`;

export const ChannelName = styled.p`
  padding: 2px 4px;

  ${({ theme, isActive }: ChannelNameStyledProps) => css`
    font-size: ${theme.FONT_SIZE.LARGE_BODY};
    background: ${isActive ? "rgb(248, 248, 248)" : "inherit"};
    background: ${isActive ? theme.COLOR.CONTAINER.GRADIENT_ORANGE : "inherit"};
  `};
`;

export const ThemeTogglerContainer = styled.div`
  position: absolute;
  left: 20px;
  bottom: 10px;
`;
