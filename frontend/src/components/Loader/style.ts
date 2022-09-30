import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.div`
  display: grid;
  justify-items: center;

  margin: 40vh 0;
`;

export const DefaultLoadingCSS = css`
  grid-area: 1/1;

  width: 50px;
  height: 50px;
  border-radius: 100%;
`;

export const LeftCircle = styled.div`
  ${DefaultLoadingCSS}

  animation: moveLeft 2s linear infinite;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.PRIMARY.DEFAULT};
  `}

  @keyframes moveLeft {
    0%,
    100% {
      margin-left: 0;
    }

    25% {
      margin-left: 250px;
    }

    75% {
      margin-left: -250px;
    }
  }
`;

export const RightCircle = styled.div`
  ${DefaultLoadingCSS}

  animation: moveRight 2s linear infinite;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.CONTAINER.LIGHT_ORANGE};
  `}

  @keyframes moveRight {
    0%,
    100% {
      margin-right: 0;
    }

    25% {
      margin-right: 250px;
    }
    50% {
      z-index: 2;
    }

    75% {
      margin-right: -250px;
    }
  }
`;
