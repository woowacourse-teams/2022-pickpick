import styled, { css } from "styled-components";
import { Theme } from "@src/@types/shared";

export const Container = styled.div`
  width: 228px;
  height: 100%;
  padding: 20px 0;

  ${({ theme }: { theme: Theme }) => css`
    background-color: ${theme.COLOR.CONTAINER.DEFAULT};
  `}
`;

export const Hr = styled.hr`
  padding: 0 10px;

  ${({ theme }: { theme: Theme }) => css`
    background-color: ${theme.COLOR.BORDER};
    height: 1px;
    border: 0;
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
