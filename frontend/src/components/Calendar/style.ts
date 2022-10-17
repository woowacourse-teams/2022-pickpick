import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

interface StyledDayProps extends StyledDefaultProps {
  isBlank: boolean;
  isToday: boolean;
  isFuture: boolean;
}

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;

  width: 280px;
  height: 313px;
  padding: 18px;

  position: fixed;
  top: 40%;
  left: 50%;

  transform: translate(-50%, -50%);
  z-index: 2;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.CONTAINER.DEFAULT};
  `}
`;

export const Month = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;

  width: 100%;
`;

export const Title = styled.h1`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.LARGE_BODY};
  `}
`;

export const Weekdays = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-around;

  width: 100%;
`;

export const Weekday = styled.p`
  width: 34px;
  height: 34px;
  display: flex;
  justify-content: center;
  align-items: center;

  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.BODY};
  `}
`;

export const Days = styled.div`
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  gap: 1px;
`;

export const Day = styled.div`
  width: 34px;
  height: 34px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  position: relative;

  div {
    position: absolute;
    width: 30px;
    height: 30px;
    border-radius: 100px;
  }

  ${({ theme, isBlank, isToday, isFuture }: StyledDayProps) => css`
    background-color: ${isBlank
      ? theme.COLOR.CONTAINER.DEFAULT
      : theme.COLOR.BACKGROUND.SECONDARY};
    font-size: ${theme.FONT_SIZE.BODY};
    pointer-events: ${isFuture || isBlank ? "none" : "auto"};
    opacity: ${isFuture ? 0.3 : 1};

    div {
      border: ${isToday ? `1px solid ${theme.COLOR.PRIMARY.DEFAULT}` : "none"};
    }
  `}
`;
