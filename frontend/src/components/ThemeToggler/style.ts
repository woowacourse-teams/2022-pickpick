import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.label`
  width: 62px;
  height: 32px;

  position: relative;
`;

export const CheckBox = styled.input`
  display: inline-block;
  appearance: none;

  width: 62px;
  height: 32px;
  border: none;
  outline: none;
  border-radius: 50px;
  cursor: pointer;
  overflow: hidden;

  position: relative;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.TEXT.DEFAULT};
  `}
`;

interface IconContainerProps extends StyledDefaultProps {
  isVisible: boolean;
}

export const LeftIconContainer = styled.div`
  cursor: pointer;

  position: absolute;
  top: -12px;
  left: 10px;

  transform: scaleY(1);
  transition: opacity cubic-bezier(0.3, 1.5, 0.7, 1) 0.5s;

  ${({ isVisible }: IconContainerProps) => css`
    opacity: ${isVisible ? "1" : "0"};
  `}
`;

export const RightIconContainer = styled.div`
  cursor: pointer;

  position: absolute;
  top: -12px;
  right: 10px;

  transform: scaleY(1);
  transition: all cubic-bezier(0.3, 1.5, 0.7, 1) 0.5s;

  ${({ isVisible }: IconContainerProps) => css`
    opacity: ${isVisible ? "1" : "0"};
  `}
`;
