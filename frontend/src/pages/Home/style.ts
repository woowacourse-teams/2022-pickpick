import styled, { css } from "styled-components";

import { StyledDefaultProps } from "@src/@types/shared";

export const Container = styled.article`
  width: 100%;
  min-width: 320px;
`;

export const GreetingContainer = styled.section`
  display: flex;
  flex-direction: column;

  max-width: 600px;
  padding: 50px 40px 40px;
  margin: 0 auto;
`;

export const UsageContainer = styled.section`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 25px;

  padding: 30px 43px;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.SECONDARY};
  `}
`;

export const UsageOrderedList = styled.ol`
  display: flex;
  flex-direction: column;
  gap: 14px;
`;

export const UsageList = styled.li`
  display: flex;
  flex-direction: column;
  gap: 14px;
`;
