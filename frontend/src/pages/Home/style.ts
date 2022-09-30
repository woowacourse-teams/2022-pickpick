import { StyledDefaultProps } from "@src/@types/shared";
import styled, { css } from "styled-components";

export const Container = styled.div`
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
  gap: 40px;

  padding: 30px 43px;

  ${({ theme }: StyledDefaultProps) => css`
    background-color: ${theme.COLOR.BACKGROUND.SECONDARY};
  `}
`;

export const UsageList = styled.ol`
  display: flex;
  flex-direction: column;
  gap: 14px;
`;
