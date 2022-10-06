import styled, { CSSProp, css } from "styled-components";

import { REMINDER_BUTTON_TEXT } from "@src/@constants";
import { StyledDefaultProps, Theme } from "@src/@types/shared";

import { ButtonText } from ".";

export const Container = styled.div`
  width: 280px;
  padding: 14px;
  border-radius: 4px;
  box-shadow: 0.5px 0.5px 2px 0px rgba(0, 0, 0, 0.1);

  position: fixed;
  top: 25%;
  left: 50%;

  transform: translate(-50%);
  z-index: 2;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.SECONDARY};
  `};
`;

export const Title = styled.h1`
  margin-bottom: 12px;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.BODY};
  `}
`;

const textTable: Record<ButtonText, CSSProp<Theme>> = {
  [REMINDER_BUTTON_TEXT.CREATE]: css`
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.WHITE};
      background-color: ${theme.COLOR.PRIMARY.DEFAULT};
    `}
  `,

  [REMINDER_BUTTON_TEXT.MODIFY]: css`
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.WHITE};
      background-color: ${theme.COLOR.PRIMARY.DEFAULT};
    `}
  `,

  [REMINDER_BUTTON_TEXT.CANCEL]: css`
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.DEFAULT};
      border: 1px solid ${theme.COLOR.BORDER};
    `}
  `,

  [REMINDER_BUTTON_TEXT.REMOVE]: css`
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
  padding: 4px 12px;
  border: none;
  border-radius: 4px;
  white-space: nowrap;
  background-color: inherit;
  cursor: pointer;

  z-index: 1;

  ${({ theme, text }: ButtonTextProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};

    ${textTable[text]}
  `}
`;
