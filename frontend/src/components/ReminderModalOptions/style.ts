import styled from "styled-components";

export const Container = styled.label`
  cursor: pointer;

  &:first-child {
    margin-top: 54px;
  }

  &:last-child {
    margin-bottom: 60px;
  }
`;

export const Radio = styled.input`
  display: none;
  position: absolute;
  opacity: 0;
`;

export const TextOption = styled.span`
  cursor: pointer;
`;
