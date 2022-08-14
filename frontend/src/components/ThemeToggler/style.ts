import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.input`
    appearance: none;
    width: 62px;
    height: 32px;
    display: inline-block;
    position: relative;
    border-radius: 50px;
    overflow: hidden;
    outline: none;
    border: none;
    cursor: pointer;

    &::before { 
    content: url('./assets/icons/MoonIcon.svg');
    display: block;
    position: absolute;
    z-index: 2;
    width: 24px;
    height: 24px;
    left: 4px;
    top: 4px;
    border-radius: 50%;
    text-indent: 4px;
    line-height: 29px;
    word-spacing: 37px;
    color: #fff;
    white-space: nowrap;
   
    transition: all cubic-bezier(0.3, 1.5, 0.7, 1) 0.3s;
    ${({ theme }: StyledDefaultProps) => css`
      color: ${theme.COLOR.TEXT.DEFAULT};
      background-color: ${theme.COLOR.CONTAINER.DEFAULT};
    `}
    }

  &:checked{
    &::before{
        left: 32px;
        content: url('./assets/icons/SunIcon.svg');
      }|
  }
`;
