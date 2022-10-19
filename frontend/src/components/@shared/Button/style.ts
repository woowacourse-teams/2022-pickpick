import styled, { CSSProp, css } from "styled-components";

import { StyledDefaultProps, Theme } from "@src/@types/shared";

import { Props, Size, StyleType } from ".";

const styleTable: Record<StyleType, CSSProp<Theme>> = {
  primary: css`
    font-weight: 600;

    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.WHITE};
      background-color: ${theme.COLOR.PRIMARY.DEFAULT};

      &: hover {
        background-color: ${theme.COLOR.CONTAINER.LIGHT_ORANGE};
      }
    `}
  `,

  secondary: css`
    font-weight: 600;

    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.PRIMARY.DEFAULT};
      border: 1px solid ${theme.COLOR.PRIMARY.DEFAULT};
      background-color: transparent;
    `}
  `,

  tertiary: css`
    font-weight: 400;

    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.DEFAULT};
      background-color: ${theme.COLOR.BACKGROUND.TERTIARY};
    `}
  `,
};

const sizeTable: Record<Size, CSSProp<Theme>> = {
  small: css`
    padding: 0.25rem 0.938rem;

    ${({ theme }: StyledDefaultProps) => css`
      font-size: ${theme.FONT_SIZE.CAPTION};
    `}
  `,
  medium: css`
    padding: 0.5rem 1.25rem;

    ${({ theme }: StyledDefaultProps) => css`
      font-size: ${theme.FONT_SIZE.BODY};
    `}
  `,
  large: css`
    padding: 0.65rem 5.875rem;

    ${({ theme }: StyledDefaultProps) => css`
      font-size: ${theme.FONT_SIZE.LARGE_BODY};
    `}
  `,
};

export const Container = styled.button`
  border: none;
  border-radius: 50px;
  white-space: nowrap;
  cursor: pointer;
  transition: 0.5s;

  ${({ size, styleType }: Pick<Props, "size" | "styleType" | "onClick">) => css`
    ${styleTable[styleType]}
    ${sizeTable[size]};
  `}
`;
