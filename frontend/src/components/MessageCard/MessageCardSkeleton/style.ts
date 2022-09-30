import { StyledDefaultProps } from "@src/@types/shared";
import styled, { keyframes, css } from "styled-components";

const refresh = keyframes`
  0% {
    background-color: rgba(165, 165, 165, 0.1);
  }
  50% {
    background-color: rgba(165, 165, 165, 0.3);
  }
  100% {
    background-color: rgba(165, 165, 165, 0.1);
  }
`;

export const Container = styled.div`
  display: flex;
  padding: 14px;
  column-gap: 4px;
  width: 100%;
  height: auto;
  border-radius: 4px;
  box-shadow: 0.5px 0.5px 2px 0px rgba(0, 0, 0, 0.1);

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.CONTAINER.DEFAULT};
  `}
`;

export const ProfileImageSkeleton = styled.div`
  width: 35px;
  height: 35px;
  border-radius: 50%;

  animation: ${refresh} 2s infinite ease-out;
`;

export const WriterSkeleton = styled.div`
  width: 71px;
  height: 14px;
  border-radius: 4px;
  animation: ${refresh} 2s infinite ease-out;
`;

export const LongLineSkeleton = styled.div`
  width: 90%;
  height: 10px;
  border-radius: 4px;
  animation: ${refresh} 2s infinite ease-out;
`;

export const ShortLineSkeleton = styled.div`
  width: 60%;
  height: 10px;
  border-radius: 4px;
  animation: ${refresh} 2s infinite ease-out;
`;
