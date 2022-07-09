import { kindTable } from "../WrapperButton/style";
import { Link } from "react-router-dom";
import styled from "styled-components";
import { Kind } from "../WrapperButton";

export const Container = styled(Link)`
  cursor: pointer;
  background-color: inherit;

  ${({ kind }: { kind?: Kind }) => kind && kindTable[kind]}
`;
