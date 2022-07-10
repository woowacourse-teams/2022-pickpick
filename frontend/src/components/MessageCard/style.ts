import { Theme } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.div`
  display: flex;
  height: auto;
  padding: 14px;
  column-gap: 4px;
  width: 17.5rem;
  border-radius: 4px;
  box-shadow: 0.5px 0.5px 2px 0px rgba(0, 0, 0, 0.1);

  ${({ theme }: { theme: Theme }) => css`
    background-color: ${theme.COLOR.CONTAINER.WHITE};
  `}
`;

export const Writer = styled.p`
  text-overflow: ellipsis;
  font-weight: 600;
  font-size: ${({ theme }: { theme: Theme }) => theme.FONT_SIZE.LARGE_BODY};
`;

export const Date = styled.p`
  ${({ theme }: { theme: Theme }) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    color: ${theme.COLOR.TEXT.PLACEHOLDER};
  `}
`;

export const Message = styled.p`
  margin-top: 3px;
  width: 193px;
  white-space: pre-wrap;
  cursor: pointer;

  ${({ theme }: { theme: Theme }) => css`
    font-size: ${theme.FONT_SIZE.BODY};
    color: ${theme.COLOR.TEXT.DEFAULT};
  `}
`;
