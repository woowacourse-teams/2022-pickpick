import styled, { css, CSSProp } from "styled-components";
import { StyledDefaultProps, Theme } from "@src/@types/shared";
import { Props, Size } from ".";

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
    padding: 0.75rem 5.875rem;

    ${({ theme }: StyledDefaultProps) => css`
      font-size: ${theme.FONT_SIZE.LARGE_BODY};
    `}
  `,
};

const colorTable = {
  active: css`
    font-weight: 600;

    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.WHITE};
      background-color: ${theme.COLOR.PRIMARY.DEFAULT};
    `}
  `,
  inactive: css`
    font-weight: 400;

    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.DEFAULT};
      background-color: ${theme.COLOR.BACKGROUND.TERTIARY};
    `}
  `,
};

export const Container = styled.button`
  border: none;
  border-radius: 50px;
  white-space: nowrap;
  cursor: pointer;

  ${({ size, isActive }: Pick<Props, "size" | "isActive" | "onClick">) => css`
    ${colorTable[isActive ? "active" : "inactive"]}
    ${sizeTable[size]};
  `}
`;
