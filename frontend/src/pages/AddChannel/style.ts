import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.div`
  display: flex;
  flex-direction: column;

  min-width: 320px;
  padding: 20px;
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
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;

  width: 100%;
  min-width: 320px;
  gap: 10px;
  margin: 40px 0;
`;

export const Button = styled.button`
  display: block;

  padding: 0.25rem 0.75rem;
  background-color: inherit;
  border-radius: 50px;
  white-space: nowrap;

  font-weight: 600;
  cursor: pointer;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.LARGE_BODY};
    border: 1px solid ${theme.COLOR.PRIMARY.DEFAULT};
  `}
`;
