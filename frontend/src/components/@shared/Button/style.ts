import styled, { css, CSSProp } from "styled-components";
import { StyledDefaultProps, Theme } from "@src/@types/shared";
import { Props, Size } from ".";

const sizeTable: Record<Size, CSSProp<Theme>> = {
  medium: css`
    font-size: ${({ theme }: StyledDefaultProps) => theme.FONT_SIZE.BODY};
    padding: 0.5rem 1.25rem;
  `,
  large: css`
    font-size: ${({ theme }: StyledDefaultProps) => theme.FONT_SIZE.LARGE_BODY};
    padding: 0.75rem 5.875rem;
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
    background-color: ${theme.COLOR.BACKGROUND.TERTIARY}};
  `}
  `,
};

export const Container = styled.button`
  border: none;
  border-radius: 50px;
  cursor: pointer;

  ${({ size, isActive }: Pick<Props, "size" | "isActive">) => css`
    ${colorTable[isActive ? "active" : "inactive"]}
    ${sizeTable[size]};
  `}
`;
