import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

export const Subtitle = styled.h2`
  font-weight: 600;
  margin-bottom: 7px;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
  `}
`;

export const TextOptionContainer = styled.div`
  display: flex;

  height: 128px;
  border-radius: 4px;

  position: relative;

  z-index: 1;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    border: 1px solid ${theme.COLOR.BORDER};
  `}

  &::before {
    display: block;

    width: 251px;
    height: 22px;
    border-radius: 3px;
    opacity: 0.4;

    position: absolute;
    top: 53px;
    left: 0;
    right: 0;

    pointer-events: none;
    content: "";

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

  overflow: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
  scrollbar-color: inherit;

  &::-webkit-scrollbar {
    display: none;
  }
`;
