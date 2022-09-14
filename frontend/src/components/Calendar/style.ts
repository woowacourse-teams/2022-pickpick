import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

interface StyledDayProps extends StyledDefaultProps {
  isBlank: boolean;
  isCurrentDay: boolean;
  isFuture: boolean;
}

export const Container = styled.div`
  width: 280px;
  height: 313px;
  display: flex;
  flex-direction: column;
  align-items: center;
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
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

export const Title = styled.h1`
  ${({ theme }: StyledDefaultProps) => css`
    font-size: ${theme.FONT_SIZE.LARGE_BODY};
  `}
`;

export const Weekdays = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
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

  ${({ theme, isBlank, isCurrentDay, isFuture }: StyledDayProps) => css`
    background-color: ${isBlank
      ? theme.COLOR.CONTAINER.DEFAULT
      : theme.COLOR.BACKGROUND.SECONDARY};
    font-size: ${theme.FONT_SIZE.BODY};

    opacity: ${isFuture ? 0.3 : 1};

    div {
      border: ${isCurrentDay
        ? `1px solid ${theme.COLOR.PRIMARY.DEFAULT}`
        : "none"};
    }
  `}
`;
