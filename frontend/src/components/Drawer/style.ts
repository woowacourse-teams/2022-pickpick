import styled, { css } from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

interface ChannelNameStyledProps extends StyledDefaultProps {
  isActive: boolean;
}

export const Container = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 228px;
  height: calc(100% - 78px);
  padding: 20px 0;
  border-radius: 0 4px 4px 0;
  z-index: 2;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.CONTAINER.DEFAULT};
  `}
`;

export const Hr = styled.hr`
  padding: 0 10px;
  height: 1px;
  border: 0;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BORDER};
  `}
`;

export const Title = styled.h1`
  font-weight: bold;

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
  bottom: 10px;
  left: 20px;
`;
