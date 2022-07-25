import styled, { css } from "styled-components";
import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 228px;
  height: calc(100% - 78px);
  padding: 20px 0;
  border-radius: 0 4px 4px 0;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.CONTAINER.DEFAULT};
  `}
`;

export const Hr = styled.hr`
  padding: 0 10px;
  height: 1px;
  border: 0;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BORDER};
  `}
`;

export const Title = styled.h1`
  font-weight: bold;

  ${({ theme }) => css`
    font-size: ${theme.FONT_SIZE.LARGE_BODY};
  `}
`;

export const ChannelName = styled.p`
  ${({ theme }) => css`
    font-size: ${theme.FONT_SIZE.LARGE_BODY};
  `}
`;
