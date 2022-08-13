import { StyledDefaultProps, Theme } from "@src/@types/shared";
import styled, { css, CSSProp } from "styled-components";
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
  z-index: 1;

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

export const Subtitle = styled.h2`
  font-weight: 600;
  margin-bottom: 7px;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
  `}
`;

export const TextContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #dcdcdc;
  border-radius: 4px;
  padding: 2px 6px;
  cursor: pointer;
  z-index: 1;
`;

export const Text = styled.p`
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

export const TextOptions = styled.div`
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

export const Label = styled.label`
  cursor: pointer;

  &:first-child {
    margin-top: 54px;
  }

  &:last-child {
    margin-bottom: 60px;
  }
`;

export const Radio = styled.input`
  position: absolute;
  top: -9999999999999999px;
  left: -9999999999999999px;
  opacity: 0;
`;

export const TextOption = styled.span`
  cursor: pointer;
`;

const textTable: Record<ButtonText, CSSProp<Theme>> = {
  생성: css`
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.WHITE};
      background-color: ${theme.COLOR.PRIMARY.DEFAULT};
    `}
  `,
  수정: css``,
  취소: css`
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.DEFAULT};
      border: 1px solid ${theme.COLOR.BORDER};
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
