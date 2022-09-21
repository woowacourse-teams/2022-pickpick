import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Subtitle = styled.h2`
  font-weight: 600;
  margin-bottom: 7px;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
  `}
`;

export const TextOptionContainer = styled.div`
  height: 128px;
  display: flex;
  border-radius: 4px;
  position: relative;
  z-index: 1;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    border: 1px solid ${theme.COLOR.BORDER};
  `}

  &::before {
    content: "";
    display: block;
    width: 251px;
    height: 22px;
    border-radius: 3px;
    position: absolute;
    top: 48px;
    left: 0;
    opacity: 0.4;
    pointer-events: none;

    ${({ theme }: StyledDefaultProps) => css`
      background-color: ${theme.COLOR.BACKGROUND.TERTIARY};
    `};
  }
`;

export const TextOptionsWrapper = styled.div`
  display: flex;
  flex-grow: 1;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  overflow: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
  scrollbar-color: inherit;

  &::-webkit-scrollbar {
    display: none;
  }
`;
