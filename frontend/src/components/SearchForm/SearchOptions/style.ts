import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 20px;
`;

export const ScrollContainer = styled.div`
  display: flex;
  gap: 5px;
  overflow: scroll;
  scrollbar-width: none;
  -ms-overflow-style: none;
  scrollbar-color: inherit;

  &::-webkit-scrollbar {
    display: none;
  }
`;

export const Text = styled.p`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.PLACEHOLDER};
  `}
`;
