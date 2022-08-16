import { StyledDefaultProps, Theme } from "@src/@types/shared";
import styled, { css, CSSProp } from "styled-components";
import { ButtonText } from "../ReminderModal";

export const Container = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 18px;
`;

const textTable: Record<ButtonText, CSSProp<Theme>> = {
  생성: css`
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.WHITE};
      background-color: ${theme.COLOR.PRIMARY.DEFAULT};
    `}
  `,

  수정: css`
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.WHITE};
      background-color: ${theme.COLOR.PRIMARY.DEFAULT};
    `}
  `,

  취소: css`
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.DEFAULT};
      border: 1px solid ${theme.COLOR.BORDER};
    `}
  `,

  삭제: css`
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.WHITE};
      background-color: ${theme.COLOR.CONTAINER.LIGHT_RED};
    `}
  `,
};

interface ButtonTextProps extends StyledDefaultProps {
  text: ButtonText;
}

export const Button = styled.button`
  border: none;
  border-radius: 4px;
  white-space: nowrap;
  cursor: pointer;
  background-color: inherit;
  padding: 4px 12px;
  z-index: 1;

  ${({ theme, text }: ButtonTextProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};

    ${textTable[text]}
  `}
`;
