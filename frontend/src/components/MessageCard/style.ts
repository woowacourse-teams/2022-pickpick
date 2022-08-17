import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 12px;
  height: auto;
  padding: 14px;
  column-gap: 4px;
  width: 100%;
  border-radius: 4px;
  box-shadow: 0.5px 0.5px 2px 0px rgba(0, 0, 0, 0.1);

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.CONTAINER.WHITE};
  `}
`;

export const Writer = styled.p`
  text-overflow: ellipsis;
  font-weight: 600;
  font-size: ${({ theme }: StyledDefaultProps) => theme.FONT_SIZE.LARGE_BODY};
`;

interface DateStyledProps extends StyledDefaultProps {
  isHighlighted: boolean;
}

export const Date = styled.p`
  font-weight: 600;

  ${({ theme, isHighlighted }: DateStyledProps) => css`
    font-size: ${theme.FONT_SIZE.CAPTION};
    color: ${isHighlighted
      ? theme.COLOR.TEXT.LIGHT_BLUE
      : theme.COLOR.TEXT.PLACEHOLDER};
  `}
`;

export const Message = styled.p`
  margin-top: 3px;
  white-space: pre-wrap;
  word-break: break-all;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.BODY};
    color: ${theme.COLOR.TEXT.DEFAULT};
  `}
`;
