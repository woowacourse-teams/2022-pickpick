import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.div`
  padding: 20px;
  display: flex;
  flex-direction: column;
  min-width: 320px;
`;

export const Title = styled.h1`
  margin-bottom: 10px;
  ${({ theme }: StyledDefaultProps) => css`
    font-family: ${theme.FONT.SECONDARY};
    font-size: ${theme.FONT_SIZE.SUBTITLE};
  `}
`;

export const Description = styled.p`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.LARGE_BODY};
  `}
`;

export const ChannelListContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  width: 100%;
  min-width: 320px;
  gap: 10px;
  margin: 40px 0;
`;

export const Button = styled.button`
  display: block;
  border-radius: 50px;
  white-space: nowrap;
  cursor: pointer;
  background-color: inherit;
  padding: 0.25rem 0.75rem;
  font-weight: 600;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.LARGE_BODY};
    border: 1px solid ${theme.COLOR.PRIMARY.DEFAULT};
  `}
`;
