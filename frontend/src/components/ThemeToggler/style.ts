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
    ${({ theme }: StyledDefaultProps) => css`
      background-color: ${theme.COLOR.TEXT.DEFAULT};
    `}
    &::before { 
      content: url('https://shivanarrthine.com/public/images/icons/moon.svg');
      display: block;
      position: absolute;
    
      width: 24px;
      height: 24px;

      left: 4px;
      top: 4px;
      border-radius: 50%;
      text-indent: 4px;
      line-height: 29px;
      word-spacing: 37px;
      white-space: nowrap;
      transition: all cubic-bezier(0.3, 1.5, 0.7, 1) 0.3s;
    }
  &:checked{
    &::before{
        left: 32px;
        content: url('https://shivanarrthine.com/public/images/icons/sun.svg');
      }|
  }
`;
